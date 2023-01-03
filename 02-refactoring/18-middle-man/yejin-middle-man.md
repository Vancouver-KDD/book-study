# Middle man

# Quick preview
클래스가 하는 일이 거의 없이 다른 클래스들에게 일을 위임하기만 할때.
message chain 을 없애려고 과하게 하다보면 발생하기도 한다.  
그래서 hide delegation 의 reverse 로 해결함.

~~~java
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

//여기서 service 가 하는 일이 repository 에게 일만 위임한다고 보는 시각이다. 


//after
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


~~~