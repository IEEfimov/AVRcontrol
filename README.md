# AVRcontrol v1.2
Android application to control AVR via ESP8266 (by TCP)


-	Допилил отпрвку как число (или несколько через пробел)
-   Допилил возможность отправки двочных чисел (0b)
-   Всвязи с этим появилось куча багов и уязвимостей в программе (ломается если не особо корректный ввод)
-   Порешал проблемы с уязвимостью программы к некорректному вводу, нихуёвый такой контроль ввода запилил
-	Запилил возможность отправки 16-чных чисел (0xAB либо ABh)
-   Обнаружил баг с горизонтальной ориентацией


		// Всю эту говнину достаточно перекинуть в "название_проекта"/app/src/
