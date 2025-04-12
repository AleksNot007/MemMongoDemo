@echo off
sc query Memcached | find "SERVICE_NAME"
if %errorlevel% neq 0 (
    C:\PROJECT_SAPR\Sapr_project\Memcached\memchached\memcached.exe -d install
)

C:\PROJECT_SAPR\Sapr_project\Memcached\memchached\memcached.exe -d start
clear
C:\PROJECT_SAPR\Sapr_project\Memcached\jdk-21\bin\java -cp "C:\PROJECT_SAPR\Sapr_project\Memcached\lib\*;" "C:\PROJECT_SAPR\Sapr_project\Memcached\source\Main.java"
C:\PROJECT_SAPR\Sapr_project\Memcached\memchached\memcached.exe -d stop
