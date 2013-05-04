@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\Rafael\workspace\AnalisadorDou\Dou_Download\teste\
set XAPP=C:\Users\Rafael\workspace\AnalisadorDou\GateDeveloperFiles\GaTE\ProcessadorInicioInicio\InicioInicio.xapp
for /F %%x in ('dir /B/D %MYDIR%') do (
  set FILENAME=%MYDIR%%%x
  java -Xmx1300M -jar Processor4.jar %XAPP% !FILENAME!
  echo ===========================  Search in !FILENAME! ===========================
)