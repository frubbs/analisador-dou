cls
@echo off 
setlocal enableDelayedExpansion 

set DIA=1
set MES=1
set ANO=2011

:loop

	set DATA=%DIA%/%MES%/%ANO%
	echo data: !DATA!
	java  -jar Downloader.jar !DATA! !DATA! C:\Users\Rafael\workspace\analisador-dou\DouDownloader\teste\
	
	if %DIA% LSS 31 (
		set /A DIA=%DIA%+1
		goto loop
	) else (
		set DIA=1
	)
	
	if %MES% LSS 12 (
		set /A MES=%MES%+1
		goto loop
	) else (
		set MES=1
	)
		
	if %ANO% GTR 2009 (
		set /A ANO=%ANO%-1
		goto loop
	) else (
		echo ACABEI"""
	)

	
