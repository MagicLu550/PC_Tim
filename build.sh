#!/usr/bin/bash

path=$(pwd)

echo $path
if [[ ! -e $path/bin ]];then
    mkdir $path/bin
fi

if [[ ! -e $path/build ]];then
    mkdir $path/build
fi


if [[ -e $path/bin/out.jar ]];then
    rm $path/bin/out.jar
fi

if [[ -e $path/build/source.txt ]];then
    rm -r $path/build/*
fi




filelist(){
for file in `ls $1|grep -v ".bak"`
  do
    if [ -d $1"/"$file ]
    then
      filelist $1"/"$file
    else
      local file_path=$1"/"$file 
      if echo $file_path|grep "MANIFEST.MF">/dev/null;then
      c=c
      else
          echo $file_path >> $path/build/source.txt
      fi
    fi
  done
}

filelist $path/src





javac -encoding utf-8 -Xlint:unchecked -d $path/build  @$path/build/source.txt

cd $path/build
jar cmf  $path/src/MANIFEST.MF  $path/bin/out.jar ./*


