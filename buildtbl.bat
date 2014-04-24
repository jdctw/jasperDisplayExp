@echo off
@rem ==============================================
@rem
@rem This batch script creates a reverse gray scale table
@rem
@rem ==============================================
del /f rev_tbl.txt
for /L %%i in (0,1,255) do call :reverse %%i 

del /f nor_tbl.txt
for /L %%i in (0,1,255) do call :normal %%i 

goto :EOF

:reverse
@echo off
set /A t=255-%1
@echo %1=%t% >> rev_tbl.txt
goto :EOF

:normal
@echo off
@echo %1=%1 >> nor_tbl.txt
goto :EOF
