package org.kodluyoruz.mybank.Account.CheckingAccount;

import org.kodluyoruz.mybank.Account.Type.AccountType;
import org.kodluyoruz.mybank.Account.Type.AccountTypeService;
import org.kodluyoruz.mybank.Customer.Customer;
import org.kodluyoruz.mybank.Customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/customer/{id}/account/type")
public class CheckingAccountController {
    private final CheckingAccountService checkingAccountService;
    private final CustomerService customerService;
    private final AccountTypeService accountTypeService;

    public CheckingAccountController(CheckingAccountService checkingAccountService, CustomerService customerService, AccountTypeService accountTypeService) {
        this.checkingAccountService = checkingAccountService;
        this.customerService = customerService;
        this.accountTypeService = accountTypeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingAccountDto create(@RequestParam String name, @PathVariable("id") int customer_id, @Valid @RequestBody CheckingAccountDto dto) throws Exception {
        CheckingAccountDto checkingAccountDto = null;
        CheckingAccount checkingAccount = dto.toCheckingDto();

        if (!name.equals("checking")) throw new Exception("Hesap tipi gecersiz.");
        if (!customerService.isCustomerExists(customer_id)) throw new Exception("Kullanici bulunamadi");

        AccountType a = accountTypeService.findAccountByName("Vadesiz");

        Customer customer = customerService.findById(customer_id);
        customer.setCheckingAccount(checkingAccount);

        if (name.equals("checking")) {
            checkingAccount.setAccountType(a);
        } else throw new Exception("Vadesiz hesap adini kontrol ediniz.");

        checkingAccountDto = checkingAccountService.create(checkingAccount).toCheckingAccountDto();
        customerService.update(customer);

        return checkingAccountDto;
    }
}