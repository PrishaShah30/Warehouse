package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        int n = id%10;
        Product sp = new Product(id, name, stock, day, demand);
        sectors[n].add(sp);
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        // IMPLEMENT THIS METHOD
        int ts = id%10;
        if (sectors[ts].getSize()!=1){
        sectors[ts].swim(sectors[ts].getSize());
        }
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
       // IMPLEMENT THIS METHOD
       int ps = id%10;
       if (sectors[ps].getSize()==5){
        sectors[ps].swap(1, 5);
        sectors[ps].deleteLast();
        sectors[ps].sink(1);
       }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        // IMPLEMENT THIS METHOD
        int i = id % 10;
        int s = sectors[i].getSize();
        for (int n = s; n > 0; n--) {
            if (sectors[i].get(n).getId() == id) {
               // if a match is found, increase the stock by the given amount and exit the loop
               sectors[i].get(n).setStock(sectors[i].get(n).getStock() + amount);
               break;
            }
         }         
    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // IMPLEMENT THIS METHOD
        int i = id % 10;
        int s = sectors[i].getSize();
        for (int n = s; n > 0; n--) {
            if (sectors[i].get(n).getId() == id) {
               // swap the matching element with the last element in the array
               sectors[i].swap(n, s);
               // delete the last element in the array
               sectors[i].deleteLast();
               // move the matching element to its proper position
               sectors[i].sink(n);
            }
         }         
    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        // IMPLEMENT THIS METHOD
        int sectorIndex = id % 10;
        int size = sectors[sectorIndex].getSize();
        for (int n = size; n > 0; n--) {
            if (sectors[sectorIndex].get(n).getId() == id) {
               // if the element's stock is greater than or equal to the amount requested, update its values
               if (sectors[sectorIndex].get(n).getStock() >= amount) {
                  sectors[sectorIndex].get(n).setStock(sectors[sectorIndex].get(n).getStock() - amount);
                  sectors[sectorIndex].get(n).setDemand(sectors[sectorIndex].get(n).getDemand() + amount);
                  sectors[sectorIndex].get(n).setLastPurchaseDay(day);
                  sectors[sectorIndex].sink(n);
               }
               break;
            }
         }         
    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        int sectorIndex = id % 10;
        int size = sectors[sectorIndex].getSize();
        do {
            if (sectors[sectorIndex].getSize() < 5) {
               sectors[sectorIndex].add(new Product(id, name, stock, day, demand));
               sectors[sectorIndex].swim(sectors[sectorIndex].getSize());
               return;
            }
            sectorIndex = (sectorIndex + 1) % 10;
         } while (sectorIndex != id % 10);
         addProduct(id, name, stock, day, demand);         
    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
