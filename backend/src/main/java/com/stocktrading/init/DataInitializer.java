package com.stocktrading.init;

import com.stocktrading.entity.AccountRelation;
import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.Holding;
import com.stocktrading.entity.SecuritiesAccount;
import com.stocktrading.entity.Stock;
import com.stocktrading.entity.User;
import com.stocktrading.entity.enums.FundAccountStatus;
import com.stocktrading.entity.enums.SecuritiesAccountStatus;
import com.stocktrading.entity.enums.StockStatus;
import com.stocktrading.entity.enums.UserRole;
import com.stocktrading.repository.AccountRelationRepository;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.HoldingRepository;
import com.stocktrading.repository.SecuritiesAccountRepository;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final FundAccountRepository fundAccountRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final AccountRelationRepository accountRelationRepository;
    private final HoldingRepository holdingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        createUserIfMissing("admin", "123456", UserRole.ADMIN, false, null);
        createUserIfMissing("staff", "123456", UserRole.STAFF, false, null);
        createUserIfMissing("user1", "123456", UserRole.USER, true, "CERT-USER1");

        seedStocks();
        seedUser1Accounts();
    }

    private void createUserIfMissing(String username, String rawPassword, UserRole role, boolean firstLogin,
        String certificateCode) {
        if (userRepository.existsByUsername(username)) {
            return;
        }
        User user = User.builder()
            .username(username)
            .password(passwordEncoder.encode(rawPassword))
            .role(role)
            .firstLogin(firstLogin)
            .certificateCode(certificateCode)
            .build();
        userRepository.save(user);
    }

    private void seedStocks() {
        List<StockSeed> seeds = List.of(
            new StockSeed("600000", "\u6d66\u53d1\u94f6\u884c", new BigDecimal("10.50")),
            new StockSeed("600519", "\u8d35\u5dde\u8305\u53f0", new BigDecimal("1800.00")),
            new StockSeed("000001", "\u5e73\u5b89\u94f6\u884c", new BigDecimal("12.30")),
            new StockSeed("000858", "\u4e94\u7cae\u6db2", new BigDecimal("130.00")),
            new StockSeed("300750", "\u5b81\u5fb7\u65f6\u4ee3", new BigDecimal("200.00"))
        );
        for (StockSeed seed : seeds) {
            if (stockRepository.existsByCode(seed.code())) {
                continue;
            }
            Stock stock = Stock.builder()
                .code(seed.code())
                .name(seed.name())
                .initialPrice(seed.price())
                .latestPrice(seed.price())
                .status(StockStatus.ACTIVE)
                .build();
            stockRepository.save(stock);
        }
    }

    private void seedUser1Accounts() {
        User user1 = userRepository.findByUsername("user1").orElse(null);
        if (user1 == null) {
            return;
        }

        FundAccount fundAccount = fundAccountRepository.findByUserId(user1.getId())
            .orElseGet(() -> fundAccountRepository.save(FundAccount.builder()
                .accountNo("FA-10001")
                .userId(user1.getId())
                .password(passwordEncoder.encode("123456"))
                .balance(new BigDecimal("100000.00"))
                .frozenBalance(BigDecimal.ZERO)
                .status(FundAccountStatus.NORMAL)
                .build()));

        SecuritiesAccount securitiesAccount = securitiesAccountRepository.findByUserId(user1.getId())
            .orElseGet(() -> securitiesAccountRepository.save(SecuritiesAccount.builder()
                .accountNo("SA-10001")
                .userId(user1.getId())
                .status(SecuritiesAccountStatus.NORMAL)
                .build()));

        accountRelationRepository
            .findByFundAccountIdAndSecuritiesAccountId(fundAccount.getId(), securitiesAccount.getId())
            .orElseGet(() -> accountRelationRepository.save(AccountRelation.builder()
                .fundAccountId(fundAccount.getId())
                .securitiesAccountId(securitiesAccount.getId())
                .build()));

        holdingRepository.findBySecuritiesAccountIdAndStockCode(securitiesAccount.getId(), "600000")
            .orElseGet(() -> holdingRepository.save(Holding.builder()
                .userId(user1.getId())
                .securitiesAccountId(securitiesAccount.getId())
                .stockCode("600000")
                .quantity(1000)
                .frozenQuantity(0)
                .build()));
    }

    private record StockSeed(String code, String name, BigDecimal price) {
    }
}
