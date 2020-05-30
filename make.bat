xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "RAYCASTER" > make.log

copy bin\RAYCASTER8.bin .\raycaster8.bin

IF EXIST raycaster.dsk GOTO :dskok
xdm99.py raycaster.dsk --initialize DSSD -n RAYCASTER
:dskok

xdm99.py raycaster.dsk -a bin/RAYCASTER
xdm99.py raycaster.dsk -a bin/RAYCASTES
xdm99.py raycaster.dsk -a bin/RAYCASTET
xdm99.py raycaster.dsk -a bin/RAYCASTEU

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf raycaster.rpk raycaster8.bin layout.xml > make.log

:end