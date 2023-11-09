set SERVER_DIR=F:\apache-tomcat-10.0.22
set DEPLOYMENT_DIR=%SERVER_DIR%\webapps
xcopy bin webapp\WEB-INF\classes /Y /E /I
jar -cvf stock.war -C webapp . 
copy  stock.war %DEPLOYMENT_DIR%