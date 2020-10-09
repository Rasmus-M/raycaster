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
    bin\texture-bank-8.bin + ^
    bin\texture-bank-9.bin + ^
    bin\texture-bank-10.bin + ^
    bin\texture-bank-11.bin + ^
    bin\texture-bank-12.bin + ^
    bin\texture-bank-13.bin + ^
    bin\texture-bank-14.bin + ^
    bin\texture-bank-15.bin + ^
    bin\texture-bank-16.bin + ^
    bin\texture-bank-17.bin + ^
    bin\texture-bank-18.bin + ^
    bin\texture-bank-19.bin + ^
    bin\texture-bank-20.bin + ^
    bin\texture-bank-21.bin + ^
    bin\texture-bank-22.bin + ^
    bin\texture-bank-23.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin ^
    .\texcaster8.bin

java -jar tools/CopyHeader.jar texcaster8.bin 60 7 8 9 10 11 12 13 14 15 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf texcaster.rpk texcaster8.bin layout.xml > make.log

:end
