1) compile
it's maven project so
mvn clean install

2) run
all classes except Executor has main method, only command line parameter is path to file

3) comments
i used parallel threads only in MostActiveUsers, but seems it was completely not optimize

4) answers

only way to check duplicates is allocate two arrays with max integer value of bits (byte[Integer.MAX_VALUE / 8] or int[Integer.MAX_VALUE / 32])
after that we can use hashCode of line as index of bit to check and update in one array for positive values and in another array for negative

i never used any java system monitoring programms but i guess jdk has some standart one