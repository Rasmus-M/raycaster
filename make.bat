xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "TEXCASTER" > make.log

copy bin\RAYCASTER8.bin .\texcaster8.bin

IF EXIST texcaster.dsk GOTO :dskok
xdm99.py texcaster.dsk --initialize DSSD -n RAYCASTER
:dskok

xdm99.py texcaster.dsk -a bin/RAYCASTER
xdm99.py texcaster.dsk -a bin/RAYCASTES
xdm99.py texcaster.dsk -a bin/RAYCASTET
xdm99.py texcaster.dsk -a bin/RAYCASTEU

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf texcaster.rpk texcaster8.bin layout.xml > make.log

:end
