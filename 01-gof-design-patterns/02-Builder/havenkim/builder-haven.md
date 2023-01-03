# Builder

### In one liner

Separate out complicated Object initialization **steps** from Class definition into a builder with multiple interfaces.

Client (or Director) do not and should not know how the object is conformed and how they handle the parameters. It only knows how to generate through AbstractBuilder interfaces and all the responsibilities are under ConcreteBuilders.

### Pros 

- AbstractBuilder mandates what the Object should contain and how it should be generated. Hence, adding a new Object type is easy to be done.
- The build() method can validate each build step and parameters it received. By doing that, data exception and logic error handling can be separated out as well.  

### Cons

- Sometimes amount of code can be overkill for "that" complexity. What if you can solve the problem using a structure with default data values?
- Builder Pattern cannot be applied if each of builder results needs more comprehensive access to its internal implementation. 

### Sample

General example representing core concept of builder pattern. [Reference](https://www.codymorterud.com/design/2020/09/03/Builder-Pattern-cpp.html)

```c++
#include <string>

class Automobile
{
public:
    struct Attributes
    {
        int numberOfTires = 0;
        std::string bodyType = "";
        std::string engineType = "";
        int fuelTankSizeInGallons = 0;
        bool airConditioned = false;
        int odometerMiles = 0;
    };

    class Builder
    {
    public:
        Builder& numberOfTires(int numberOfTires) {
            this->atrributes.numberOfTires = numberOfTires;
            return *this;
        }

        Builder& bodyType(const std::string& bodyType) {
            this->atrributes.bodyType = bodyType;
            return *this;
        }

        Builder& engineType(const std::string& engineType) {
            this->atrributes.engineType = engineType;
            return *this;
        }

        Builder& fuelTankSizeInGallons(int fuelTankSizeInGallons) {
            this->atrributes.fuelTankSizeInGallons = fuelTankSizeInGallons;
            return *this;
        }

        Builder& airConditioned(bool airConditioned) {
            this->atrributes.airConditioned = airConditioned;
            return *this;
        }

        Builder& odometerMiles(int odometerMiles) {
            this->atrributes.odometerMiles = odometerMiles;
            return *this;
        }

        Automobile build() {
            return Automobile(atrributes);
        }
    private:
        Automobile::Attributes atrributes;
    };
private:
    Automobile(Attributes newAttributes) :
        mAttributes(newAttributes) {};
    Attributes mAttributes;
};

int main() {
    Automobile automobile = Automobile::Builder()
        .numberOfTires(4)
        .bodyType("coupe")
        .engineType("V8")
        .fuelTankSizeInGallons(16)
        .airConditioned(true)
        .odometerMiles(20000)
        .build();
}
```

There is no rule of defining builders but putting into the class itself feels more practical since Builder is in 1:1 relationship.

### IRL examples

JSON Serialization classes usually contains Director (JSON token parsing) and Builder (Generate JSON Object) concepts.

Decodable protocol in Swift

```swift
struct BlogPost: Decodable {
    enum Category: String, Decodable {
        case swift, combine, debugging, xcode
    }

    enum CodingKeys: String, CodingKey {
        case title, category, views
        // Map the JSON key "url" to the Swift property name "htmlLink"
        case htmlLink = "url"
    }

    let title: String
    let htmlLink: URL
    let category: Category
    let views: Int
}


let blogPost: BlogPost = try! JSONDecoder().decode(BlogPost.self, from: jsonData)
print(blogPost.htmlLink) // Prints: "https://www.avanderlee.com/swift/optionals-in-swift-explained-5-things-you-should-know/"
```

Depends on which JSON element type parsed, it fills out specified attributes or leave it empty if nothing has detected.

[Reference](https://www.avanderlee.com/swift/json-parsing-decoding/)