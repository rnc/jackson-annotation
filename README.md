[![Build Status (Travis CI)](https://travis-ci.org/rnc/jackson-annotation.svg?branch=master)](https://travis-ci.org/rnc/jackson-annotation.svg?branch=master)



# Jackson Post Serialization Annotation

## Overview

This code provides a simple annotation that may be placed upon a method (even a private method) which will be run upon completion of the deserialization.

This has been inspired by several stackoverflow posts:

* [Jackson Mapper post-construct](https://stackoverflow.com/questions/6834677/jackson-mapper-post-construct/6834831#6834831)
* [How do I call the default deserializer from a custom deserializer in Jackson](https://stackoverflow.com/questions/18313323/how-do-i-call-the-default-deserializer-from-a-custom-deserializer-in-jackson/18405958#18405958)
* [How to make @JsonBackReference and a custom deserializer work at the same time?](https://stackoverflow.com/questions/55924605/jackson-how-to-make-jsonbackreference-and-a-custom-deserializer-work-at-the-sa)

and by the following Jackson issues
* [Issue 279](https://github.com/FasterXML/jackson-databind/issues/279)
* [Issue 2045](https://github.com/FasterXML/jackson-databind/issues/2045)