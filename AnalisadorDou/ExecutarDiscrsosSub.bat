cls
@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\Rafael\workspace\analisador-dou\Discursos\txt\
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\ProcessadorDiscursos\ProcessadorDiscurso.xapp

dir /A:-D-S/S %MYDIR%
pause
echo comecou!


for /F "delims=" %%x in ('dir /B/A:-D-S/S %MYDIR%') do (

 SET TEXT=%%x
 SET SUBSTRING=!TEXT:~-6!


	echo processar : %%x 
	java -jar ProcessDiscurso.jar %XAPP% "%%x" >> C:\Users\Rafael\workspace\analisador-dou\AnalisadorDou\log\loging.syso.log
 
)
echo terminou!