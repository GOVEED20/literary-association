package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.Reader;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.repositories.ReaderRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckTransactionsService {
    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private WriterRepository writerRepository;

    @SneakyThrows
    @Scheduled(cron = "0 0 1 * * *")
    public void deleteExpiredTransactions() {
        checkTransactionsForReaders();
        checkTransactionsForWriters();
    }

    @SneakyThrows
    @Async
    public void checkTransactionsForWriters() {
        writerRepository.deleteAll(
                writerRepository.findByMembershipApprovedIsTrue()
                        .stream()
                        .filter(this::checkMembershipTransactions).collect(Collectors.toList())
        );
    }

    @SneakyThrows
    public boolean checkMembershipTransactions(Writer writer) {
        Calendar cal = Calendar.getInstance();

        if (writer.getTransactions().stream().anyMatch(mt -> {
            if (mt.getStatus().equals(TransactionStatus.CREATED)) {
                cal.setTime(mt.getCreatedOn());
                cal.add(Calendar.WEEK_OF_MONTH, 2);

                return cal.getTime().compareTo(new Date()) > 0;
            } else if (mt.getStatus().equals(TransactionStatus.INITIALIZED)) {
                cal.setTime(mt.getInitializedOn());
                cal.add(Calendar.MINUTE, 20);

                return cal.getTime().compareTo(new Date()) > 0;
            }
            return false;
        })) {
            log.info(String.format("Removing writer with username '%s' due to unpaid membership", writer.getUsername()));
            return true;
        }
        return false;
    }

    @SneakyThrows
    @Async
    public void checkTransactionsForReaders() {
        readerRepository.findAll().forEach(this::checkTransactionsForReader);
    }

    @SneakyThrows
    public void checkTransactionsForReader(Reader reader) {
        Calendar cal = Calendar.getInstance();

        reader.getTransactions()
                .removeIf(t -> {
                    if (t.getStatus().equals(TransactionStatus.INITIALIZED)) {
                        cal.setTime(t.getInitializedOn());
                        cal.add(Calendar.MINUTE, 20);

                        if (new Date().compareTo(cal.getTime()) > 0) {
                            log.info(String.format("Removed expired transaction with id '%d'", t.getId()));
                            return true;
                        }
                    }
                    return false;
                });
        readerRepository.save(reader);
    }
}
