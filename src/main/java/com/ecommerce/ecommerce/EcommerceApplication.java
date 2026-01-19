package com.ecommerce.ecommerce;

import com.ecommerce.ecommerce.entity.Category;
import com.ecommerce.ecommerce.entity.Produto;
import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder encoder,
            CategoryRepository categoryRepository,
            ProdutoRepository produtoRepository
    ) {
        return args -> {
            System.out.println("🔄 Inicializando dados...");

            try {
                // 1. Criar roles se não existirem
                Optional<Role> roleClienteOpt = roleRepository.findByNome("ROLE_CLIENTE");
                Role roleCliente = roleClienteOpt.orElseGet(() -> {
                    Role r = new Role("ROLE_CLIENTE");
                    return roleRepository.save(r);
                });

                Optional<Role> roleAdminOpt = roleRepository.findByNome("ROLE_ADMIN");
                Role roleAdmin = roleAdminOpt.orElseGet(() -> {
                    Role r = new Role("ROLE_ADMIN");
                    return roleRepository.save(r);
                });

                System.out.println("✅ Roles verificadas/criadas");

                // 2. Criar admin se não existir
                Optional<User> adminOpt = userRepository.findByEmail("admin@email.com");
                if (adminOpt.isEmpty()) {
                    User admin = new User();
                    admin.setNome("Administrador");
                    admin.setEmail("admin@email.com");
                    admin.setSenha(encoder.encode("admin123"));
                    admin.setRoles(Set.of(roleAdmin));
                    userRepository.save(admin);
                    System.out.println("✅ Admin criado: admin@email.com / admin123");
                }

                // 3. Criar cliente se não existir
                Optional<User> clienteOpt = userRepository.findByEmail("cliente@email.com");
                if (clienteOpt.isEmpty()) {
                    User cliente = new User();
                    cliente.setNome("João Cliente");
                    cliente.setEmail("cliente@email.com");
                    cliente.setSenha(encoder.encode("123456"));
                    cliente.setRoles(Set.of(roleCliente));
                    userRepository.save(cliente);
                    System.out.println("✅ Cliente criado: cliente@email.com / 123456");
                }

                // 4. Criar categorias se não existirem
                System.out.println("🔍 Verificando categorias no banco...");
                long categoryCount = categoryRepository.count();
                System.out.println("📊 Total de categorias encontradas: " + categoryCount);
                
                if (categoryCount == 0) {
                    System.out.println("📝 Criando 3 categorias padrão...");
                    Category eletronicos = new Category("Eletrônicos", "Produtos eletrônicos");
                    Category informatica = new Category("Informática", "Computadores e acessórios");
                    Category moveis = new Category("Móveis", "Móveis para casa");

                    categoryRepository.save(eletronicos);
                    System.out.println("✅ Categoria 1 salva: Eletrônicos (ID: " + eletronicos.getId() + ")");
                    
                    categoryRepository.save(informatica);
                    System.out.println("✅ Categoria 2 salva: Informática (ID: " + informatica.getId() + ")");
                    
                    categoryRepository.save(moveis);
                    System.out.println("✅ Categoria 3 salva: Móveis (ID: " + moveis.getId() + ")");
                    
                    long newCount = categoryRepository.count();
                    System.out.println("✅ 3 categorias criadas. Total agora: " + newCount);
                } else {
                    System.out.println("ℹ️ Categorias já existem no banco. Pulando seeding.");
                }

                // 5. Criar produtos se não existirem
                if (produtoRepository.count() == 0) {
                    Optional<Category> catEletronicos = categoryRepository.findByNome("Eletrônicos");
                    Optional<Category> catInformatica = categoryRepository.findByNome("Informática");

                    if (catEletronicos.isPresent()) {
                        Produto smartphone = new Produto();
                        smartphone.setNome("Smartphone XYZ");
                        smartphone.setShortDescription("Smartphone de última geração");
                        smartphone.setLongDescription("Tela 6.7\", 256GB, 12GB RAM");
                        smartphone.setPreco(new BigDecimal("2999.99"));
                        smartphone.setQuantidadeEstoque(50);
                        smartphone.setCategoria(catEletronicos.get());
                        smartphone.setSku("SMART-XYZ-001");
                        smartphone.setEmPromocao(true); // ✅ Boolean, não boolean
                        smartphone.setPrecoPromocional(new BigDecimal("2599.99"));
                        smartphone.setEmDestaque(true); // ✅ Boolean, não boolean

                        produtoRepository.save(smartphone);
                        System.out.println("✅ Produto 1 criado: Smartphone XYZ");
                    }

                    if (catInformatica.isPresent()) {
                        Produto notebook = new Produto();
                        notebook.setNome("Notebook Pro");
                        notebook.setShortDescription("Notebook para trabalho");
                        notebook.setLongDescription("Intel i7, 16GB RAM, SSD 512GB");
                        notebook.setPreco(new BigDecimal("4999.99"));
                        notebook.setQuantidadeEstoque(30);
                        notebook.setCategoria(catInformatica.get());
                        notebook.setSku("NOTE-PRO-001");
                        notebook.setOfertaDoDia(true);

                        produtoRepository.save(notebook);
                        System.out.println("✅ Produto 2 criado: Notebook Pro");
                    }
                }

                System.out.println("🎉 Inicialização concluída com sucesso!");

            } catch (Exception e) {
                System.err.println("❌ Erro na inicialização: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}