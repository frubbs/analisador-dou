@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\TempDir2\
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\Processador\InicioInicio_v5.xapp
for /F %%x in ('dir /B/D %MYDIR%') do (
  set FILENAME=%MYDIR%%%x
  java -Xmx1300M -jar Processor5.jar %XAPP% !FILENAME!
  echo ===========================  Search in !FILENAME! ===========================
)