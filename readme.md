# Quarkus Base Architecture
[![Javadoc](https://camo.githubusercontent.com/a499b156975bcb01dbf3881f6157b247f5a4670d78d16755504531e2128dd604/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a617661446f632d4f6e6c696e652d677265656e)](https://trajy.github.io/Quarkus-Base-Architecture/br/com/trajy/architecture/base/package-summary.html)
[![Licence MIT](https://camo.githubusercontent.com/a4426cbe5c21edb002526331c7a8fbfa089e84a550567b02a0d829a98b136ad0/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4c6963656e73652d4d49542d79656c6c6f772e737667)](https://opensource.org/licenses/MIT)


This project contains boilerplate code to turn simple quarkus rest-api development. Uses the [Strategy Design Pattern](https://refactoring.guru/pt-br/design-patterns/strategy) and [Template Method Design Pattern](https://refactoring.guru/pt-br/design-patterns/template-method) to define different behaviors for each application domain. See [How to install](./package-readme.md) guide to add the project maven dependency.

## Summary
- [Quarkus Base Architecture](#quarkus-base-architecture)
  - [Summary](#summary)
  - [Controller Implementation](#controller-implementation)
  - [Assembly Implementation](#assembly-implementation)
  - [Service Implementation](#service-implementation)
    - [Override methods to call static Hibernate with Panache implementations](#override-methods-to-call-static-hibernate-with-panache-implementations)
    - [Bussiness logic exemple](#bussiness-logic-exemple)
  - [Entity implementation](#entity-implementation)

## Controller Implementation

A Rest controller that provides an end-point with [CRUD Operations](https://www.basedash.com/blog/what-are-crud-operations-in-a-rest-api) (http verbs GET, CREATE, UPDATE and DELETE) can be easily implemented just by inserting respectively the types of the `Data Transfer Object (DTO)`, `Entity`, `Service`, and `Assembly` classes as arguments in the diamond operator ```<>```. 

```java
@Path("/foos")
public class FooController extends AbstractPanacheController<FooDTO, FooEntity, FooService, FooAssembler> {

}
```
Note that `@Path` has `/foos` as argument, so the end-points will be:
Http Verb | Uri
---------|------
GET | `/foos` 
CREATE | `/foos` 
UPDATE | `/foos/{id}` 
DELETE | `/foos/{id}` 

If is necessary implement some code before or after `Service` bussiness logic, override the before or after methods.

In following exemple a property of object received from `CREATE` request is changed before it is passed to service layer.

```java
@Path("/foos")
public class FooController extends AbstractPanacheController<FooDTO, FooEntity, FooService, FooAssembler> {

    @Override
    public void beforeCreate(FooDto dto) {
        dto.someProperty = "new value";
    }

}
```

>See [JavaDoc](#quarkus-base-architecture) to abstract classes details.

## Assembly Implementation
The assembler must have overridden methods to perform the mapping between `Entity` and `DTO` classes, for `UPDATE` it must map the object attached to the database to the object with the new data as shown in the example.

```java
@ApplicationScoped
public class FooAssembler extends AbstractPanacheAssembly<FooDTO, FooEntity> {

    @Override
    public FooEntity assembly(FooDTO dto) {
        return FooEntity.builder()
                .name(dto.name)
                .age(dto.age)
                .build();
    }

    @Override
    public FooDTO assembly(FooEntity entity) {
        return FooDTO.builder()
                .id(entity.id)
                .name(entity.name)
                .age(entity.age)
                .build();
    }

    @Override
    public void fillToAttachedEntity(FooEntity attached, FooEntity detached) {
        attached.name = detached.name;
        attached.age = detached.age;
    }

}
```

>**NOTE**: Additionally, the [MapStruct](https://mapstruct.org/news/2019-12-06-mapstruct-and-quarkus/) dependency can help to automatically assembly code generation.

## Service Implementation

Similarly to [Controller Implementation](#controller-implementation), `Service` class need extends `AbstractPanacheService` type and passes the `Entity` type as an argument to the diamond operator `<>`.

### Override methods to call static Hibernate with Panache implementations
However, due to the way quarkus works, to avoid the use of reflection, there are 3 methods that must be overridden with the call to static methods through the entities that inherit from PanacheEntity.

```java
@ApplicationScoped
public class FooService extends AbstractPanacheService<FooEntity> {

    @Override
    protected List<FooEntity> findStaticMethod() {
        return FooEntity.<FooEntity>findAll().stream().toList();
    }

    @Override
    protected FooEntity findByIdStatidMethod(Long id) {
        return FooEntity.findById(id);
    }

    @Override
    protected void deleteByIdStaticMethod(Long id) {
        FooEntity.deleteById(id);
    }

}
```

### Bussiness logic exemple

The bussiness logic can be declared into before and after methods.

```java
@ApplicationScoped
public class FooService extends AbstractPanacheService<FooEntity> {

    @Override
    public beforeUpdade(Long id, FooEntity enitity) {
        // ...some bussiness logic
    }

    @Override
    public afterUpdate(Long id, FooEntity entity) {
        // ...some bussiness logic
    }
    
    // ...Overriden static call methods

}
```

## Entity implementation

Classes annotated with `@Entity` must extend the `PanacheEntity` class.

```java
@Entity
@Table(name = "foo")
public class FooEntity extends PanacheEntity {

    public String name;

    public Integer age;

}
```
