now=`date +%Y%m%d%H%M%S`
tomcat_program1="/usr/local/chigua_1";
target_war="/root/.jenkins/jobs/athene-tomcat1/workspace/Source/Server/Athene/Athene-wizard/target/athene.war";
war_name="athene"

echo "#############开始##############";

mv /root/.jenkins/jobs/athene-tomcat1/workspace/Source/Server/Athene/Athene-wizard/target/Frame.war /root/.jenkins/jobs/athene-tomcat1/workspace/Source/Server/Athene/Athene-wizard/target/athene.war

echo "正在杀死tomcat1进程"
kill -9 $(ps -ef | grep /usr/local/chigua_1 | grep -v grep | awk '{print $2}')

echo "正在删除tomcat1的webapps"
rm -rf ${tomcat_program1}/webapps/* ;

echo "#################开始部署程序###################";
cp -r ${target_war} ${tomcat_program1}/webapps/${war_name}.war

echo "####重启tomcat1...." ;
${tomcat_program1}/bin/startup.sh;
sleep 60;
ps -ef | grep tomcat

