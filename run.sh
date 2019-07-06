#!/usr/bin/bash
path=$(pwd)
for java_lib in $(ls $path/plugin/libs);do
if [[ libs != "" ]];then
    libs=$libs:$path/plugin/libs/$java_lib
else
    libs=$path/plugin/libs/$java_lib
fi
done



if ! which java >/dev/null;then
    echo "java没装运行你妹啊";
    exit
fi


java -Dfile.encoding=utf-8 -Xbootclasspath/a:$libs -jar $path/bin/out.jar $qqnum $qqpasswd $websocket_server
