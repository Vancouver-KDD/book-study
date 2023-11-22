# Property-Based Testing
- The code passes the tests, because it does what it is supposed to based on your understanding.
- Instead, we favor an alternative, where the computer, which doesn’t share your preconceptions, does some testing for you.


## CONTRACTS, INVARIANTS, AND PROPERTIES
> Tip 71 Use Property-Based Tests to Validate Your Assumptions

```python
    // proptest/sort.py

    from   hypothesis import given
    import hypothesis.strategies as some
    
    @given(some.lists(some.integers()))
    def test_list_size_is_invariant_across_sorting(a_list):
    original_length = len(a_list)
    a_list.sort()
    assert len(a_list) == original_length
  
    @given(some.lists(some.text()))   
    def test_sorted_result_is_ordered(a_list):       
    a_list.sort()       
    for i in range(len(a_list) - 1):           
    assert a_list[i] <= a_list[i + 1]
```

```
$ pytest  sort.py   
======================= test session starts ========================   
...   
plugins: hypothesis-4.14.0
  
sort.py ..                                                    [100%]
===================== 2 passed in 0.95 seconds =====================
```
- Not much drama there. But, behind the scenes, Hypothesis ran both of our tests one hundred times, passing in a different list each time. 
- The lists will have varying lengths, and will have different contents. 
- It’s as if we’d cooked up 200 individual tests with 200 random lists

## TEST DATA GENERATION 
- @given(some.integers())
- @given(some.integers(min_value=5, max_value=10).map(lambda x: x * 2))
- @given(some.lists(some.integers(min_value=1), max_size=100))

## FINDING BAD ASSUMPTIONS 
```python
// proptest/stock.py

class Warehouse:       
    def __init__(self, stock):           
        self.stock = stock
     
    def in_stock(self, item_name):
        return (item_name in self.stock) and (self.stock[item_name] > 0)
    
    def take_from_stock(self, item_name, quantity):           
        if quantity <= self.stock[item_name]:               
            self.stock[item_name] -= quantity           
        else:             
            raise Exception("Oversold {}".format(item_name))
      
    def stock_count(self, item_name):           
        return self.stock[item_name]

// test 1      
    def test_warehouse():
        wh = Warehouse({"shoes": 10, "hats": 2, "umbrellas": 0})
        assert wh.in_stock("shoes")
        assert wh.in_stock("hats")
        assert not wh.in_stock("umbrellas")
        
        wh.take_from_stock("shoes", 2)
        assert wh.in_stock("shoes")
        
        wh.take_from_stock("hats", 2)
        assert not wh.in_stock("hats")

// add function
    def order(warehouse, item, quantity):       
        if warehouse.in_stock(item):           
            warehouse.take_from_stock(item, quantity)           
            return ( "ok", item, quantity )       
        else:           
            return ( "not available", item, quantity )

        
// test 2
    def test_order_in_stock():       
        wh = Warehouse({"shoes": 10, "hats": 2, "umbrellas": 0})       
        status, item, quantity = order(wh, "hats", 1)       
        assert status   == "ok"       
        assert item     == "hats"       
        assert quantity == 1       
        assert wh.stock_count("hats") == 1
    
    def test_order_not_in_stock():       
        wh = Warehouse({"shoes": 10, "hats": 2, "umbrellas": 0})       
        status, item, quantity = order(wh, "umbrellas", 1)       
        assert status   == "not available"       
        assert item     == "umbrellas"       
        assert quantity == 1       
        assert wh.stock_count("umbrellas") == 0

    def test_order_unknown_item():       
        wh = Warehouse({"shoes": 10, "hats": 2, "umbrellas": 0})       
        status, item, quantity = order(wh, "bagel", 1)       
        assert status   == "not available"       
        assert item     == "bagel"       
        assert quantity == 1
        
// property-based test
    @given(item     = some.sampled_from(["shoes", "hats"]),          
           quantity = some.integers(min_value=1, max_value=4))
           
    def test_stock_level_plus_quantity_equals_original_stock_level(item,quantity):       
        wh = Warehouse({"shoes": 10, "hats": 2, "umbrellas": 0})       
        initial_stock_level = wh.stock_count(item)       
        (status, item, quantity) = order(wh, item, quantity)       
        if status == "ok":           
        assert wh.stock_count(item) + quantity == initial_stock_level
```
```
$ pytest stock.py  
...   stock.py:72:  
__________________________________   
stock.py:76: in test_stock_level_plus_quantity_equals_original_stock_level       
    (status, item, quantity) = order(wh, item, quantity)   
stock.py:40: in order       
    warehouse.take_from_stock(item, quantity)  
__________________________________
self = <stock.Warehouse object at 0x10cf97cf8>, item_name = 'hats'   
quantity = 3
    def take_from_stock(self, item_name, quantity):         
        if quantity <= self.stock[item_name]:           
            self.stock[item_name] -= quantity         
        else:   
>       raise Exception("Oversold {}".format(item_name))
E       Exception: Oversold hats

stock.py:16: Exception   
---------------------------- Hypothesis ----------------------------   
Falsifying example:    test_stock_level_plus_quantity_equals_original_stock_level(
    item='hats', quantity=3)
    
// fixed version
    def in_stock(self, item_name):
        return (item_name in self.stock) and (self.stock[item_name] > quantity)
        
    def order(warehouse, item, quantity):       
        if warehouse.in_stock(item, quantity):           
            warehouse.take_from_stock(item, quantity)           
            return ( "ok", item, quantity )       
        else:           
            return ( "not available", item, quantity )
```

## PROPERTY-BASED TESTS OFTEN SURPRISE YOU 
- Our suggestion is that when a property-based test fails, 
  - find out what parameters it was passing to the test function, 
  - and then use those values to create a separate, regular, unit test.

1. First, it lets you focus in on the problem without all the additional calls being made into your code by the property-based testing framework.
2. Second, that unit test acts as a regression test. Because property-based tests generate random values that get passed to your test, 
   - there’s no guarantee that the same values will be used the next time you run tests. 
   - Having a unit test that forces those values to be used ensures that this bug won’t slip through.

## PROPERTY-BASED TESTS ALSO HELP YOUR DESIGN 
- When we talked about unit testing, we said that one of the major benefits was the way it made you think about your code: a unit test is the first client of your API.
- The same is true of property-based tests, but in a slightly different way.
  - This extra insight has a magical effect on your code, removing edge cases and highlighting functions that leave data in an inconsistent state.

## CHALLENGES
- Think about the code you’re currently working on. What are the properties: 
- the contracts and invariants? Can you use property-based testing framework to verify these automatically?