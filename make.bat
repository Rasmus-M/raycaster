xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "TEXCASTER" > make.log

cd TexGen
call make.bat
cd ..

cd textures
rem call make.bat
cd ..

@copy /b bin\RAYCASTER8.bin + ^
    bin\col-bank-4.bin + ^
    bin\col-bank-5.bin + ^
    bin\col-bank-6.bin + ^
    bin\col-bank-7.bin + ^
    bin\col-bank-8.bin + ^
    bin\col-bank-9.bin + ^
    bin\col-bank-10.bin + ^
    bin\col-bank-11.bin + ^
    bin\sprite-bank-12.bin + ^
    bin\sprite-bank-13.bin + ^
    bin\sprite-bank-14.bin + ^
    bin\sprite-bank-15.bin + ^
    bin\sprite-bank-16.bin + ^
    bin\sprite-bank-17.bin + ^
    bin\sprite-bank-18.bin + ^
    bin\sprite-bank-19.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\texture-bank-0.bin + ^
    bin\texture-bank-1.bin + ^
    bin\texture-bank-2.bin + ^
    bin\texture-bank-3.bin + ^
    bin\texture-bank-4.bin + ^
    bin\texture-bank-5.bin + ^
    bin\texture-bank-6.bin + ^
    bin\texture-bank-7.bin + ^
    bin\empty-2k.bin + backgrounds\background-00-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-01-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-02-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-03-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-04-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-05-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-06-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-07-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-00-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-01-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-02-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-03-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-04-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-05-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-06-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-07-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-10-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-11-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-12-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-13-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-14-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-15-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-16-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-17-even.bin + ^
    bin\empty-2k.bin + backgrounds\background-10-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-11-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-12-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-13-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-14-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-15-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-16-odd.bin + ^
    bin\empty-2k.bin + backgrounds\background-17-odd.bin ^
    .\texcaster8.bin

java -jar tools/CopyHeader.jar texcaster8.bin 60 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf texcaster.rpk texcaster8.bin layout.xml > make.log

:end
