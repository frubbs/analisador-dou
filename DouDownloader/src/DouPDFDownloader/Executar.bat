::================================================
:: TOMORROW.BAT
::
:: Function to return tomorrow's date
::================================================
@echo off

set /a d=%date:~0,2%
set /a m=%date:~3,2%
set /a y=%date:~6,4%

:loop
  set /a d+=1

  if %d% gtr 31 (
    set d=1
    set /a m+=1
     
    if %m% gtr 12 (
      set m=1
      set /a y+=1
    )
  )
xcopy /d:%m%-%d%-%y% /l . .. >nul 2>&1 || goto loop

echo %d%/%m%/%y%