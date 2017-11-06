now=`date +%Y%m%d%H%M%S`
tomcat_program2="/usr/local/chigua_2";
bak_dir="/mnt/backup";
target_war="/root/.jenkins/jobs/athene-tomcat2/workspace/Source/Server/Athene/Athene-wizard/target/athene.war";

war_name="athene"
################延时函数########################
echo "#############开始##############";

mv /root/.jenkins/jobs/athene-tomcat2/workspace/Source/Server/Athene/Athene-wizard/target/Frame.war /root/.jenkins/jobs/athene-tomcat2/workspace/Source/Server/Athene/Athene-wizard/target/athene.war

# 杀掉chigua2的进程
kill -9 $(ps -ef | grep /usr/local/chigua_2 | grep -v grep | awk '{print $2}')

echo "###########开始备份###################";
mv ${tomcat_program2}/webapps/${war_name}.war ${bak_dir}/${war_name}_$(date "+%Y%m%d_%H%M").war;
rm -rf ${tomcat_program2}/webapps/* ;

echo "###########即将重启 ###################";
sleep 30;

echo "#################开始部署程序###################";
cp -r ${target_war} ${tomcat_program2}/webapps/${war_name}.war

echo "####重启tomcat2...." ;
${tomcat_program2}/bin/startup.sh;
sleep 60;
ps -ef | grep tomcat


