xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "TEXCASTER" > make.log

xas99.py -b -w textures/texture-bank-0.a99 -o bin/texture-bank-0.bin
xas99.py -b -w textures/texture-bank-1.a99 -o bin/texture-bank-1.bin
xas99.py -b -w textures/texture-bank-2.a99 -o bin/texture-bank-2.bin
xas99.py -b -w textures/texture-bank-3.a99 -o bin/texture-bank-3.bin
xas99.py -b -w textures/texture-bank-4.a99 -o bin/texture-bank-4.bin
xas99.py -b -w textures/texture-bank-5.a99 -o bin/texture-bank-5.bin
xas99.py -b -w textures/texture-bank-6.a99 -o bin/texture-bank-6.bin
xas99.py -b -w textures/texture-bank-7.a99 -o bin/texture-bank-7.bin

copy /b bin\RAYCASTER8.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    TexGen\col-bank7.bin + ^
    TexGen\col-bank8.bin + ^
    TexGen\col-bank9.bin + ^
    TexGen\col-bank10.bin + ^
    TexGen\col-bank11.bin + ^
    TexGen\col-bank12.bin + ^
    TexGen\col-bank13.bin + ^
    TexGen\col-bank14.bin + ^
    TexGen\col-bank15.bin + ^
    bin\empty-2k.bin + backgrounds\background0even.bin + ^
    bin\empty-2k.bin + backgrounds\background1even.bin + ^
    bin\empty-2k.bin + backgrounds\background2even.bin + ^
    bin\empty-2k.bin + backgrounds\background3even.bin + ^
    bin\empty-2k.bin + backgrounds\background4even.bin + ^
    bin\empty-2k.bin + backgrounds\background5even.bin + ^
    bin\empty-2k.bin + backgrounds\background6even.bin + ^
    bin\empty-2k.bin + backgrounds\background7even.bin + ^
    bin\empty-2k.bin + backgrounds\background0odd.bin + ^
    bin\empty-2k.bin + backgrounds\background1odd.bin + ^
    bin\empty-2k.bin + backgrounds\background2odd.bin + ^
    bin\empty-2k.bin + backgrounds\background3odd.bin + ^
    bin\empty-2k.bin + backgrounds\background4odd.bin + ^
    bin\empty-2k.bin + backgrounds\background5odd.bin + ^
    bin\empty-2k.bin + backgrounds\background6odd.bin + ^
    bin\empty-2k.bin + backgrounds\background7odd.bin + ^
    bin\texture-bank-0.bin + ^
    bin\texture-bank-1.bin + ^
    bin\texture-bank-2.bin + ^
    bin\texture-bank-3.bin + ^
    bin\texture-bank-4.bin + ^
    bin\texture-bank-5.bin + ^
    bin\texture-bank-6.bin + ^
    bin\texture-bank-7.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin ^
    .\texcaster8.bin

java -jar tools/CopyHeader.jar texcaster8.bin 60 7 8 9 10 11 12 13 14 15 32 33 34 35 36 37 38 39

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf texcaster.rpk texcaster8.bin layout.xml > make.log

:end
