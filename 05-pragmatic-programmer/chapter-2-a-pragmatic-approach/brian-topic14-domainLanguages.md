# Domain Languages

- We always try to write code using the vocabulary of the application domain.
- In some cases, Pragmatic Programmers can go to the next level and actually program using the vocabulary, syntax, and semantics - the language - of the domain.

## Tip 22 - Program Close to the Problem Domain
1. Internal Domain languages: RSpec for Ruby
   - RSpec is a testing library for Ruby.
     - Behaviour Driven Development for Ruby. 
```
RSpec.describe 'Addition operation' do
  it '1 + 1 = 2' do
    expect(1 + 1).to eq 2
  end
end
```
2. External Domain languages: Cucumber
   - Cucumber is programming-language neutral way of specifying tests.
      - Cucumber is a tool that supports Behaviour-Driven Development(BDD).
```
Feature: Addition
    In order to avoid silly mistakes
    As a math idiot
    I want to be told the sum of two numbers

   Scenario Outline: Add two numbers
       Given I have entered <input_1> into the calculator
       And I have entered <input_2> into the calculator
       When I press <button>
       Then the result should be <output> on the screen
   
       Examples:
       | input_1 | input_2 | button | output |
       | 20      | 30      | add    | 50     |
       | 2       | 5       | add    | 7      |
       | 0       | 40      | add    | 40     |   
```
3. Internal Domain languages: Phoenix router for Elixir
   - The route provides a set of macros for generating routes that dispatch to specific controllers and actions.
```
defmodule MyAppWeb.Router do
  use Phoenix.Router

  get "/pages/:page", PageController, :show
end
```
```
$ mix phx.routes
page_path  GET    /pages/:id       PageController.show/2
user_path  GET    /users           UserController.index/2
user_path  GET    /users/:id/edit  UserController.edit/2
user_path  GET    /users/new       UserController.new/2
user_path  GET    /users/:id       UserController.show/2
user_path  POST   /users           UserController.create/2
user_path  PATCH  /users/:id       UserController.update/2
           PUT    /users/:id       UserController.update/2
```
4. External Domain languages: Ansible
   - Ansible is a tool that configures software, typically on a bunch of remote serers. (like Fabric for Python?)
```
---   
- name: install nginx
  apt: name=nginx state=latest
- name: ensure nginx is running (and enable it at boot)
  service: name=nginx state=started enabled=yes
- name: write the nginx config file
  template: src=templates/nginx.conf.j2 dest=/etc/nginx/nginx.conf
  notify:
  - restart nginx
```

## Trade-Offs Between Internal and External Languages
|              | Internal                             | External                       | 
|--------------|--------------------------------------|--------------------------------|
| Advantage    | more powerful than the host language | dependent on the host language |
| Disadvantage | no restrict                          | hard to create a parser        |

## Challenges
- Could some of the requirements of your current project be expressed in a domain-specific language? 
  - Would it be possible to write a compiler or translator that could generate most of the code required?
- If you decide to adopt mini-languages as a way of programming closer to the problem domain, 
  - you’re accepting that some effort will be required to implement them. 
  - Can you see ways in which the framework you develop for one project can be reused in others?