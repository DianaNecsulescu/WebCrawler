This project represents a web crawler which gets all the links
from the page "https://tomblomfield.com/" as well as the links
form the children pages and so on. The crawler only gets the 
links under the "https://tomblomfield.com/", but it could be
easily adapted to get links from other domains too.

To connect to the web pages and parse their content, the 
project uses Jsoup library. Jsoup is an open source library
which can be used to extract data from a HTML document. The 
parsing of HTML pages could have been done without any library,
but I have chose this option in order to keep the code short
and clean.

The application uses an executor service in order to create 
a thread pool and execute tasks in parallel. The number of 
the threads form the thread pool is equal with the number
of available processors of the machine to obtain the maximum
speedup. The number of threads used could be easily changed 
if suitable.

The application starts the executor service and then submits
a task for each individual link from the domain. A link
can be visited only once in order to avoid cycles. Each task
visits a link and then creates and submits new tasks for 
children links. 

After all the valid links are visited once, the executor
service is shutdown and then the resulting json is composed
and printed at standard output. The project uses the jackson-databind
library in order to pretty print the json.

The project can be built using Maven by running the 
'mvn clean install' command at the root of the project.
This will generate a Jar with all the dependencies included
which can be run with 'java -jar <path_to_jar>' command.
