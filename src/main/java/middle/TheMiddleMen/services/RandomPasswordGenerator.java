package middle.TheMiddleMen.services;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class RandomPasswordGenerator {

    public String generatePassowrd(){
        StringBuilder password = new StringBuilder();
        for(int i = 0; i< 15;i++){
            password.append(randomCharacterGenerator());
        }

        return new String(password);

    }

    public Character randomCharacterGenerator(){
        Random random = new Random();
        return (char) random.nextInt(40,150);
    }
}
