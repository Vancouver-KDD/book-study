# Message chains 

## Quick preview 
한 가지 요청을 하면 다른 object 부르고 또 그안에서 다른 object 부르고 이런식으로 계속 계속 전파되어서 해야할때. 

### example
[code smells](https://www.baeldung.com/cs/code-smells)
~~~java
//before
class Repository {
    Entity findById(long id) {
        // implementation
    }
}

class Service {
    Repository repository;

    Repository getRepository() {
        return repository;
    }
}

class Context {
    Service service;

    void useCase() {
        // the following is a message chain
        Entity entity = service.getRepository().findById(1);
        // using entity
    }
}

//after
class Service {
    Repository repository;

    Entity findById(long id) {
        return repository.findById(id);
    }
}

class Context {
    Service service;

    void useCase() {
        Entity entity = service.findById(1);
        // using entity (Hide delegation) 
    }
}
~~~


