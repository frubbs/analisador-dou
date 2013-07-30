cls
@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\Rafael\Desktop\MestradoApresentacao1\Metodo2\PDF2604\txt
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\Processador\InicioInicio_Educacao_v1.xapp
for /F %%x in ('dir /B/A:-D/S %MYDIR%') do (
  set FILENAME=%MYDIR%%%x
  java -Xmx1300M -jar ProcessorPDF.jar %XAPP% !FILENAME!
  echo ===========================  Search in !FILENAME! ===========================
)