@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM ------------------
@REM   JAVA_HOME - location of a JDK home dir
@REM   M2_HOME - location of Maven's home
@REM   MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo on
@setlocal enabledelayedexpansion

@REM set title of command window
title %0

@REM enable echoing by setting MAVEN_BATCH_ECHO to 'on'
@if "%MAVEN_BATCH_ECHO%" == "on"  echo %MAVEN_BATCH_ECHO%

@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

@REM Execute a user defined script before this one
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPre
@REM check for pre script, once with legacy .bat ending and once with .cmd ending
if exist "%USERPROFILE%\mavenrc_pre.bat" call "%USERPROFILE%\mavenrc_pre.bat" %*
if exist "%USERPROFILE%\mavenrc_pre.cmd" call "%USERPROFILE%\mavenrc_pre.cmd" %*
:skipRcPre

@setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
goto okJavaHome

: noJavaHome
echo.
echo Error: JAVA_HOME is not defined correctly. >&2
echo We cannot execute %0 >&2
goto error

:okJavaHome

@REM ==== END VALIDATION ====

if not "%M2_HOME%" == "" goto setM2Home
:stripM2Home
if exist "%M2_HOME%\bin\mvn.cmd" goto init

@REM ====== RESOLVE M2_HOME ======
@REM Search for Maven home directory
set "MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%"
if "%MAVEN_PROJECTBASEDIR%" == "" set "MAVEN_PROJECTBASEDIR=%cd%"

set "EXEC_DIR=%cd%"
set "WDIR=%EXEC_DIR%"
:findM2Home
if exist "%WDIR%\bin\mvn.cmd" goto foundM2Home
cd ..
set "WDIR=%cd%"
if "%WDIR%" == "%EXEC_DIR%" goto notFoundM2Home
goto findM2Home

:foundM2Home
set "M2_HOME=%WDIR%"
cd "%EXEC_DIR%"
goto init

:notFoundM2Home
echo.
echo Error: M2_HOME is not defined correctly. >&2
echo We cannot execute %0 >&2
goto error

:init
@REM ==== START INIT ====
if not "%M2_HOME%" == "" (
  set "M2_HOME=%WDIR%"
)
SET "M2_HOME=%M2_HOME:\=/%"

:stripM2Home
if exist "%M2_HOME%\bin\mvn.cmd" goto init2
if exist "%M2_HOME%\bin\mvn.bat" goto init2
if exist "%M2_HOME%\mvn\bin\mvn.cmd" goto init2
if exist "%M2_HOME%\mvn\bin\mvn.bat" goto init2

@REM ====== RESOLVE M2_HOME ======
@REM Search for Maven home directory
set "MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%"
if "%MAVEN_PROJECTBASEDIR%" == "" set "MAVEN_PROJECTBASEDIR=%cd%"

set "EXEC_DIR=%cd%"
set "WDIR=%EXEC_DIR%"
:findM2Home2
if exist "%WDIR%\bin\mvn.cmd" goto foundM2Home2
if exist "%WDIR%\bin\mvn.bat" goto foundM2Home2
cd ..
set "WDIR=%cd%"
if "%WDIR%" == "%EXEC_DIR%" goto notFoundM2Home2
goto findM2Home2

:foundM2Home2
set "M2_HOME=%WDIR%"
cd "%EXEC_DIR%"
goto init2

:notFoundM2Home2
echo.
echo Error: M2_HOME is not defined correctly. >&2
echo We cannot execute %0 >&2
goto error

:init2
@REM ==== END INIT ====

@REM Find the java.exe
if not "%JAVACMD%" == "" (
  if not "%JAVA_HOME%" == "" (
    set "JAVACMD=java"
  ) else (
    if exist "%JAVA_HOME%\jre\bin\java.exe" (
      set "JAVACMD=%JAVA_HOME%\jre\bin\java.exe"
    ) else (
      set "JAVACMD=%JAVA_HOME%\bin\java.exe"
    )
  )
)

if not exist "%JAVACMD%" (
  echo.
  echo Error: JAVA_HOME is not defined correctly. >&2
  echo We cannot execute %0 >&2
  goto error
)

@REM ==== START EXECUTION ====

@REM -- Windows NT specific --
if "%OS%" == "Windows_NT" set "LOCAL_ERRORLEVEL=%ERRORLEVEL%"

set "MAVEN_CMD_LINE_ARGS=%*"

@REM -- Windows 9x specific --
if not "%OS%" == "Windows_NT" set "MAVEN_CMD_LINE_ARGS=%MAVEN_CMD_LINE_ARGS:=""

@REM Reaching here means variables are defined and arguments have been captured
@endInit & goto main

:error
set ERROR_CODE=1

@endInit
@REM ==== END EXEC ====

if not "%MAVEN_SKIP_RC%" == "" goto skipRcPost
@REM check for post script, once with legacy .bat ending and once with .cmd ending
if exist "%USERPROFILE%\mavenrc_post.bat" call "%USERPROFILE%\mavenrc_post.bat"
if exist "%USERPROFILE%\mavenrc_post.cmd" call "%USERPROFILE%\mavenrc_post.cmd"
:skipRcPost

@REM pause the script if MAVEN_BATCH_PAUSE is set to 'on'
if "%MAVEN_BATCH_PAUSE%" == "on" pause

if "%MAVEN_TERMINATE_CMD%" == "on" exit %ERROR_CODE%

exit /B %ERROR_CODE%

:main
@REM The following lines are only required if not using the Launcher
@REM They are read by the launcher script and define the command to run
set CLASSWORLDS_JAR=""
if not "%CLASSWORLDS%" == "" (
  if exist "%M2_HOME%\lib\ext\classworlds.jar" (
    set "CLASSWORLDS_JAR=-classpath %M2_HOME%\lib\ext\classworlds.jar"
  )
  if exist "%M2_HOME%\lib\classworlds.jar" (
    set "CLASSWORLDS_JAR=-classpath %M2_HOME%\lib\classworlds.jar"
  )
)

set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

set "DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

FOR /F "usebackq tokens=1,2 delims==" %%A IN ('"%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"') DO (
    IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

@REM Extension to allow automatically downloading the maven-wrapper.jar from Maven-central
@REM This allows using the maven wrapper in projects that prohibit checking in binary data.
if exist %WRAPPER_JAR% (
    if "%MVNW_VERBOSE%" == "true" (
        echo Found %WRAPPER_JAR%
    )
) else (
    if not "%MVNW_REPOURL%" == "" (
        SET "MVNW_REPOURL=https://repo.maven.apache.org/maven2"
    )
    if "%MVNW_VERBOSE%" == "true" (
        echo Couldn't find %WRAPPER_JAR%, downloading it ...
        echo Downloading from: %MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
    )

    powershell -Command "&{"^
        "$webclient = new-object System.Net.WebClient"^
        "if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%')) -and -not ([string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
            "$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%')"^
        "}"^
        "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12"^
        "$webclient.DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')"^
    "}"
    if "%MVNW_VERBOSE%" == "true" (
        echo Finished downloading %WRAPPER_JAR%
    )
)
@REM End of extension

@REM Provide a "standardized" way to retrieve the CLI args that will
@REM work with both Windows and non-Windows executions.
set "MAVEN_CMD_LINE_ARGS=%MAVEN_CMD_LINE_ARGS%"

%MAVEN_JAVA_EXE% %JVM_CONFIG% %MAVEN_OPTS% %CLASSWORLDS_JAR% "-Dmaven.home=%M2_HOME%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set "ERROR_CODE=%ERROR_CODE%"
