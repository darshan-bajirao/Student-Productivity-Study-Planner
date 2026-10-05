@echo off
setlocal
where java >nul 2>&1
if errorlevel 1 (
  echo Java was not found. Install JDK 21 and ensure it is on PATH.
  pause
  exit /b 1
)
if exist out rmdir /s /q out
mkdir out
echo Compiling FINAL polished project...
javac -encoding UTF-8 -d out src\Main.java src\model\*.java src\service\*.java src\ui\*.java
if errorlevel 1 (
  echo.
  echo COMPILATION FAILED.
  pause
  exit /b 1
)
echo Compilation successful.
java -cp out Main
pause
