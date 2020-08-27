xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "TEXCASTER" > make.log

xas99.py -b textures/texture-bank-0.a99
xas99.py -b textures/texture-bank-1.a99

copy /b bin\RAYCASTER8.bin + ^
    texture-bank-0.bin + ^
    texture-bank-1.bin + ^
    TexGen\col-bank6.bin + ^
    TexGen\col-bank7.bin + ^
    TexGen\col-bank8.bin + ^
    TexGen\col-bank9.bin + ^
    TexGen\col-bank10.bin + ^
    TexGen\col-bank11.bin + ^
    TexGen\col-bank12.bin + ^
    TexGen\col-bank13.bin + ^
    TexGen\col-bank14.bin + ^
    bin\empty.bin ^
    .\texcaster8.bin

java -jar tools/CopyHeader.jar texcaster8.bin 60 4 5 6 7 8 9 10 11 12 13 14

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf texcaster.rpk texcaster8.bin layout.xml > make.log

:end
