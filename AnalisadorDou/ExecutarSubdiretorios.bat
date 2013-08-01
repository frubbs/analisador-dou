cls
@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\Rafael\Desktop\MestradoApresentacao1\PDF2604\txt
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\ProcessadorPDF\ProcessadorPDF_v5.xapp
for /F %%x in ('dir /B/A:-D-S/S %MYDIR%') do (
 java -Xmx1300M -jar ProcessFile.jar %XAPP% %%x >> execucao.txt
  
)