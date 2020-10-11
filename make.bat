xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "TEXCASTER" > make.log

cd TexGen
rem call make.bat
cd ..

cd textures
rem call make.bat
cd ..

@copy /b bin\RAYCASTER8.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\col-bank7.bin + ^
    bin\col-bank8.bin + ^
    bin\col-bank9.bin + ^
    bin\col-bank10.bin + ^
    bin\col-bank11.bin + ^
    bin\col-bank12.bin + ^
    bin\col-bank13.bin + ^
    bin\col-bank14.bin + ^
    bin\col-bank15.bin + ^
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
    bin\sprite-bank-40.bin + ^
    bin\sprite-bank-41.bin + ^
    bin\sprite-bank-42.bin + ^
    bin\sprite-bank-43.bin + ^
    bin\sprite-bank-44.bin + ^
    bin\sprite-bank-45.bin + ^
    bin\sprite-bank-46.bin + ^
    bin\sprite-bank-47.bin + ^
    bin\sprite-bank-48.bin + ^
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

java -jar tools/CopyHeader.jar texcaster8.bin 60 7 8 9 10 11 12 13 14 15 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf texcaster.rpk texcaster8.bin layout.xml > make.log

:end
