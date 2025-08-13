import groovy.xml.XmlParser

class Example {
    static void main(String[] args) {
        //Initializing 2 variables
        int x = 2;
        int X = 10;

        //Printing the value of the variables to the console
        println("The value of x is $x. The value of X is $X.")

        def range = x..X;
        println(range.toList());

        for (int i in x..X) {
            println(i);
        }

        String a = 'Hello Single';
        String b = "Hello Double";
        String c = "'Hello Triple" + "Multiple lines'";
        println(b)
        println(a[x])
        def list1 = [1, 2, 3]
        List list2 = [1, 2, 3]
        list2 <<4

        println(list2)

        def mp = ["TopicName" : "Maps", "TopicDescription" : "Methods in Maps"]
        mp.each {println it}
//        mp.each {println "${it.key} maps to: ${it.value}"}

        println(list2.findAll {it>2})



    }
}
