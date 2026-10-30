package com.toystore.common;

import com.toystore.component1_user.service.UserService;
import com.toystore.component2_toy.service.ToyService;
import com.toystore.component4_admin.service.AdminService;
import com.toystore.component5_supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminService adminService;
    private final UserService userService;
    private final ToyService toyService;
    private final SupplierService supplierService;

    @Autowired
    public DataInitializer(AdminService adminService, UserService userService, ToyService toyService, SupplierService supplierService) {
        this.adminService = adminService;
        this.userService = userService;
        this.toyService = toyService;
        this.supplierService = supplierService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Ensure upload directories exist
        Files.createDirectories(Paths.get("uploads", "toys"));
        Files.createDirectories(Paths.get("uploads", "users"));
        Files.createDirectories(Paths.get("uploads", "suppliers"));

        // Only seed if files are empty
        if (isFileEmpty("admins.txt")) {
            System.out.println("Seeding default admin...");
            adminService.registerAdmin("admin", "admin123", "admin@toyslanka.com", "ALL");
        }
        
        if (isFileEmpty("users.txt")) {
            System.out.println("Seeding sample users...");
            userService.registerRegularUser("kamal", "password", "kamal@email.lk", "REGULAR");
            userService.registerRegularUser("janith", "password", "janith@email.lk", "PREMIUM");
        }
        
        if (isFileEmpty("toys.txt")) {
            System.out.println("Seeding sample toys...");
            toyService.addToy("LEGO Classic Bricks", "LEGO", "EDUCATIONAL", "3-5", 8900.00, 50);
            toyService.addToy("Remote Control Car", "Hot Wheels", "ELECTRONIC", "6-9", 13600.00, 30);
            toyService.addToy("Giant Teddy Bear", "Build-A-Bear", "PLUSH", "0-3", 10500.00, 20);
            toyService.addToy("Math Puzzle Set", "Melissa & Doug", "EDUCATIONAL", "6-9", 5900.00, 100);
            toyService.addToy("Wooden Building Blocks", "Fisher-Price", "NON_ELECTRONIC", "3-5", 4500.00, 40);
        }
        
        if (isFileEmpty("suppliers.txt")) {
            System.out.println("Seeding sample suppliers...");
            supplierService.addSupplier("Local Toys Dist", "local@dist.com", "Melissa & Doug, Hot Wheels", "LOCAL");
            supplierService.addSupplier("Global Brands Inc", "global@brands.com", "LEGO, Hasbro", "INTERNATIONAL");
        }
    }

    private boolean isFileEmpty(String fileName) {
        try {
            return Files.readAllLines(Paths.get("data", fileName)).isEmpty();
        } catch (Exception e) {
            return true;
        }
    }
}
