@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\DouDownload\TempDir2012\
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\Processador\InicioInicio_Educacao_v1.xapp
for /F %%x in ('dir /B/D %MYDIR%') do (
  set FILENAME=%MYDIR%%%x
  java -Xmx1300M -jar ProcessorEducacao.jar %XAPP% !FILENAME!
  echo ===========================  Search in !FILENAME! ===========================
)