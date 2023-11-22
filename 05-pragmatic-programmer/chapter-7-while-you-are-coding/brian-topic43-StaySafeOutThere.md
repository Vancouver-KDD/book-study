# Stay Safe Out There

> Good fences make good neighbors.
> - Robert Frost, Mending Wall

- As we write this, the daily news is filled with stories of devastating data breaches, hijacked systems, and cyberfraud. 
-  In the vast majority of cases, it’s not because the attackers were terribly clever, or even vaguely competent.
- It’s because the developers were careless.

## THE OTHER 90% 
- You’re 90% done, but now you have the other 90% to consider.
- The next thing you have to do is analyze the code for ways it can go wrong and add those to your test suite.
- Internal errors are may going to be no problem. 
- Perhaps you protest, 
  - “Oh, no one will care about this code, it’s not important, no one even knows about this server…” 
  - It’s a big world out there, and most of it is connected.

## SECURITY BASIC PRINCIPLES 
1. Minimize Attack Surface Area 
2. Principle of Least Privilege 
3. Secure Defaults 
4. Encrypt Sensitive Data
5. Maintain Security Updates

### Minimize Attack Surface Area 
- The attack surface area of a system is the sum of all access points where an attacker can enter data, extract data, or invoke execution of a service.
1. Code complexity leads to attack vectors
2. Input data is an attack vector
```
puts "Enter a file name to count: "   
name = gets   
system("wc -c #{name}")
```
```
// Problem
Enter a file name to count:   
test.dat; rm -rf /
```
```
// Solve
>> $SAFE=1

puts "Enter a file name to count: "   
name = gets   
system("wc -c #{name}")
```
3. Unauthenticated services are an attack vector
4. Authenticated services are an attack vector
5. Output data is an attack vector
   - "Password is used by another user." Don’t give away information.
6. Debugging info is an attack vector

> Tip 72 - Keep It Simple and Minimize Attack Surfaces

### Principle of Least Privilege
- Another key principle is to use the least amount of privilege for the shortest time you can get away with.
> Every program and every privileged user of the system should operate using the least amount of privilege necessary to complete the job.
> 
> — Jerome Saltzer, Communications of the ACM, 1974.

### Secure Defaults 
- Ex) Show asterisk to hide the password as entered

### Encrypt Sensitive Data
- Don’t check in secrets, API keys, SSH keys, encryption passwords or other credentials alongside your source code in version control.
- Keys and secrets need to be managed separately, generally via config files or environment variables as part of build and deployment.

### Maintain Security Updates 
- The largest data breaches in history (so far) were caused by systems that were behind on their updates.
- Don’t let it happen to you.

> Tip 73 - Apply Security Patches Quickly

## COMMON SENSE VS. CRYPTO 
- It’s important to keep in mind that common sense may fail you when it comes to matters of cryptography. 
- The first and most important rule when it comes to crypto is never do it yourself.
  - your clever new, home-made encryption algorithm can probably be broken by an expert in minutes. You don’t want to do encryption yourself.

- As we’ve said elsewhere, rely only on reliable things: well- vetted, thoroughly examined, well-maintained, frequently updated, preferably open source libraries and frameworks.

