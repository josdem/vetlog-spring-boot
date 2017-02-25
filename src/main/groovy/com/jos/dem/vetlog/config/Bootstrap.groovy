package com.jos.dem.vetlog.config

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.core.env.Environment

import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.model.Role
import com.jos.dem.vetlog.model.Breed
import com.jos.dem.vetlog.enums.CurrentEnvironment
import com.jos.dem.vetlog.repository.UserRepository
import com.jos.dem.vetlog.repository.BreedRepository

@Component
class Bootstrap implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  Environment environment
  @Autowired
  UserRepository userRepository
  @Autowired
  BreedRepository breedRepository

  @Override
  void onApplicationEvent(final ApplicationReadyEvent event) {
    if(environment.activeProfiles[0] == CurrentEnvironment.DEVELOPMENT.getDescription()){
      println "Loading development environment"
      createDefaultUsers()
      createBreeds()
    }
  }

  void createDefaultUsers(){
    createUserWithRole('josdem', '12345678', 'joseluis.delacruz@gmail.com', Role.USER)
    createUserWithRole('admin', '12345678', 'admin@email.com', Role.ADMIN)
  }

  void createBreeds(){
    def breeds = [
      new Breed(name:'Labrador', type:'DOG'),
      new Breed(name:'Landrace', type:'DOG'),
      new Breed(name:'German Shepherd', type:'DOG'),
      new Breed(name:'Golden Retriever', type:'DOG'),
      new Breed(name:'Bulldog', type:'DOG'),
      new Breed(name:'Poodle', type:'DOG'),
      new Breed(name:'Beagle', type:'DOG'),
      new Breed(name:'Boxer', type:'DOG'),
      new Breed(name:'Yorkshire Terrier', type:'DOG'),
      new Breed(name:'Rottweiler', type:'DOG'),
      new Breed(name:'Chihuahua', type:'DOG'),
      new Breed(name:'Dachshund', type:'DOG'),
      new Breed(name:'French Bulldog', type:'DOG'),
      new Breed(name:'Shih Tzu', type:'DOG'),
      new Breed(name:'Pug', type:'DOG'),
      new Breed(name:'Siberian Husky', type:'DOG'),
      new Breed(name:'English Cocker Spaniel', type:'DOG'),
      new Breed(name:'Pomeranian', type:'DOG'),
      new Breed(name:'Cavalier King Charles Spaniel', type:'DOG'),
      new Breed(name:'Border Collie', type:'DOG'),
      new Breed(name:'Bull Terrier', type:'DOG'),
      new Breed(name:'Schnauzer', type:'DOG'),
      new Breed(name:'Great Dane', type:'DOG'),
      new Breed(name:'Bull Terrier', type:'DOG'),
      new Breed(name:'Old English Sheepdog', type:'DOG'),
      new Breed(name:'Doberman', type:'DOG'),
      new Breed(name:'Jack Russell Terrier', type:'DOG'),
      new Breed(name:'Maltese', type:'DOG'),
      new Breed(name:'Newfoundland dog', type:'DOG'),
      new Breed(name:'English Mastiff', type:'DOG'),
      new Breed(name:'Australian Shepherd', type:'DOG'),
      new Breed(name:'Boston Terrier', type:'DOG'),
      new Breed(name:'Havanese', type:'DOG'),
      new Breed(name:'Dalmatian', type:'DOG'),
      new Breed(name:'Pointer', type:'DOG'),
      new Breed(name:'St. Bernard', type:'DOG'),
      new Breed(name:'Pit bull', type:'DOG'),
      new Breed(name:'Weimaraner', type:'DOG'),
      new Breed(name:'Dogue de Bordeaux', type:'DOG')
    ]
    if(!breedRepository.findAll().size()){
      breeds.each {
        breedRepository.save(it)
      }
    }
  }

  void createUserWithRole(String username, String password, String email, Role authority) {
    if(!userRepository.findByUsername(username)){
      User user = new User(
        username:username,
        password:new BCryptPasswordEncoder().encode(password),
        email:email,
        role:authority,
        firstname:username,
        lastname:username,
        enabled:true
      )
      userRepository.save(user)
    }
  }

}
