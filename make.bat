xas99.py -R -S -L raycaster.lst -w src/raycaster.a99 -o raycaster.obj
@IF %ERRORLEVEL% NEQ 0 GOTO :end

xas99.py -R -i -w src/raycaster.a99 -o bin/RAYCASTER

java -jar tools/ea5tocart.jar bin/RAYCASTER "TEXCASTER" > make.log

xas99.py -b -w src/map.a99 -o bin/map.bin

cd TexGen
rem call make.bat
cd ..

cd textures
rem call make.bat
cd ..

cd panel
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
    bin\sprite-bank-11.bin + ^
    bin\sprite-bank-12.bin + ^
    bin\sprite-bank-13.bin + ^
    bin\sprite-bank-14.bin + ^
    bin\sprite-bank-15.bin + ^
    bin\sprite-bank-16.bin + ^
    bin\texture-bank-0.bin + ^
    bin\texture-bank-1.bin + ^
    bin\texture-bank-2.bin + ^
    bin\texture-bank-3.bin + ^
    bin\texture-bank-4.bin + ^
    bin\texture-bank-5.bin + ^
    bin\texture-bank-6.bin + ^
    bin\texture-bank-7.bin + ^
    bin\texture-bank-8.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\empty.bin + ^
    bin\map.bin + bin\empty-2k.bin + bin\empty-2k.bin + ^
    bin\bottom-panel.bin + ^
    backgrounds\background-100.bin + backgrounds\background-110.bin + ^
    backgrounds\background-101.bin + backgrounds\background-111.bin + ^
    backgrounds\background-102.bin + backgrounds\background-112.bin + ^
    backgrounds\background-103.bin + backgrounds\background-113.bin + ^
    backgrounds\background-104.bin + backgrounds\background-114.bin + ^
    backgrounds\background-105.bin + backgrounds\background-115.bin + ^
    backgrounds\background-106.bin + backgrounds\background-116.bin + ^
    backgrounds\background-107.bin + backgrounds\background-117.bin + ^
    backgrounds\background-000.bin + backgrounds\background-010.bin + ^
    backgrounds\background-001.bin + backgrounds\background-011.bin + ^
    backgrounds\background-002.bin + backgrounds\background-012.bin + ^
    backgrounds\background-003.bin + backgrounds\background-013.bin + ^
    backgrounds\background-004.bin + backgrounds\background-014.bin + ^
    backgrounds\background-005.bin + backgrounds\background-015.bin + ^
    backgrounds\background-006.bin + backgrounds\background-016.bin + ^
    backgrounds\background-007.bin + backgrounds\background-017.bin + ^
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

java -jar tools/CopyHeader.jar texcaster8.bin 60 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47

WHERE jar
@IF %ERRORLEVEL% NEQ 0 GOTO :end
jar -cvf texcaster.rpk texcaster8.bin layout.xml > make.log

:end
