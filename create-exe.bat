@echo off
setlocal

REM === Configuration ===
set APP_NAME=FabioParty
set MAIN_JAR=FabioParty.jar
set MAIN_CLASS=personal.projectparty.app.MainCLI
set VERSION=1.0.0
set VENDOR=FabioBrocs
set ICON=icon.ico

REM === Paths ===
set INPUT_DIR=app\build\libs
set OUTPUT_DIR=portable-exe

REM === BUILD ===
echo Eseguo shadowJar per rigenerare il JAR...
call gradlew shadowJar || goto :error

REM === Rinomino il jar all'output desiderato ===
echo Rinomino il JAR risultante...
for %%F in (%INPUT_DIR%\*-all.jar) do (
    move "%%F" "%INPUT_DIR%\%JAR_NAME%" >nul
)

REM === Create Portable App Image ===
echo Creating portable EXE...

jpackage ^
  --name %APP_NAME% ^
  --input %INPUT_DIR% ^
  --main-jar %MAIN_JAR% ^
  --main-class %MAIN_CLASS% ^
  --type app-image ^
  --app-version %VERSION% ^
  --vendor "%VENDOR%" ^
  --win-console ^
  --icon %ICON% ^
  --dest %OUTPUT_DIR%

echo.
echo Done! Check the '%OUTPUT_DIR%\%APP_NAME%' folder for your portable app.
pause
