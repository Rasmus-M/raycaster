xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "RAYCASTER" > make.log

copy bin\RAYCASTER8.bin .\raycaster3-8.bin

IF EXIST raycaster.dsk GOTO :dskok
xdm99.py raycaster.dsk --initialize DSSD -n RAYCASTER
:dskok

xdm99.py raycaster.dsk -a bin/RAYCASTER

rem WHERE jar
rem @IF %ERRORLEVEL% NEQ 0 GOTO :end
rem jar -cvf tix.rpk tix8.bin layout.xml > make.log

:end