cls
@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\Rafael\Desktop\MestradoApresentacao1\Metodo2b\txt\
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\ProcessadorPDF\ProcessadorPDF_v5.xapp

dir /A:-D-S/S %MYDIR%
pause
echo comecou!


for /F %%x in ('dir /B/A:-D-S/S %MYDIR%') do (

 SET TEXT=%%x
 SET SUBSTRING=!TEXT:~-6!

 IF "!SUBSTRING!" == "-1.txt" (
	echo processar : %%x 
	java -Xmx1300M  -jar ProcessFile.jar %XAPP% %%x >> C:\Users\Rafael\Desktop\MestradoApresentacao1\Metodo2b\loging.syso.log
 ) else (
	echo Nao vamos processar arquivos do segundo ou terceiro jornal: %%x
 ) 
 
)
echo terminou!