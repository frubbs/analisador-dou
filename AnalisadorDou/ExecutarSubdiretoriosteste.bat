cls
@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\Rafael\Desktop\MestradoApresentacao1\Metodo2b\txt\
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\ProcessadorPDF\ProcessadorPDF_v5.xapp
echo comecou!
for /F %%x in ('dir /B/A:-D-S/S %MYDIR%') do (

 SET TEXT=%%x
 SET SUBSTRING=!TEXT:~-6!

 IF "!SUBSTRING!" == "-1.txt" (
	echo processar : %%x  >> dirArquivosAprocessar2.txt

 )
)
echo terminou!