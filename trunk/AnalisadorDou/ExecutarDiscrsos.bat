@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\Rafael\workspace\analisador-dou\Discursos\txt\
set XAPP=C:\Users\Rafael\workspace\analisador-dou\GateDeveloperFiles\ProcessadorDiscursos\ProcessadorDiscurso.xapp
for /f "delims=" %%x in ('dir /B/D %MYDIR%') do (
  set FILENAME=%MYDIR%%%x
  java  -jar ProcessDiscurso.jar %XAPP% "!FILENAME!"
  echo ===========================  Search in !FILENAME! ===========================
)
