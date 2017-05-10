package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.validation.Valid

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetImage
import com.jos.dem.vetlog.model.PetType
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.PetCommand
import com.jos.dem.vetlog.validator.PetValidator
import com.jos.dem.vetlog.service.BreedService
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.PetImageService
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.client.S3Writer

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/pet")
class PetController {

  @Autowired
  BreedService breedService
  @Autowired
  PetService petService
  @Autowired
  PetImageService petImageService
  @Autowired
  UserService userService
  @Autowired
  LocaleService localeService
  @Autowired
  S3Writer s3Writer

  @Value('${breedsByTypeUrl}')
  String breedsByTypeUrl
  @Value('${bucketDestination}')
  String bucketDestination
  @Value('${awsImageUrl}')
  String awsImageUrl

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(new PetValidator())
  }

  @RequestMapping(method = GET, value = "/create")
  ModelAndView create(){
    ModelAndView modelAndView = new ModelAndView('pet/create')
    Command petCommand = new PetCommand()
    modelAndView.addObject('petCommand', petCommand)
    fillModelAndView(modelAndView)
  }

  @Transactional
  @RequestMapping(method = POST, value = "/save")
  ModelAndView save(@Valid PetCommand petCommand, BindingResult bindingResult) {
    log.info "Creating pet: ${petCommand.name}"
    ModelAndView modelAndView = new ModelAndView('pet/create')
    if (bindingResult.hasErrors()) {
      modelAndView.addObject('petCommand', petCommand)
      return fillModelAndView(modelAndView)
    }
    User user = userService.getCurrentUser()
    PetImage petImage = petImageService.save()
    petCommand.images.add(petImage)
    Pet pet = petService.save(petCommand, user)
    s3Writer.uploadToBucket(bucketDestination, petImage.uuid, petCommand.image.getInputStream())
    modelAndView.addObject('message', localeService.getMessage('pet.created'))
    petCommand = new PetCommand()
    modelAndView.addObject('petCommand', petCommand)
    fillModelAndView(modelAndView)
  }

  ModelAndView fillModelAndView(ModelAndView modelAndView){
    modelAndView.addObject('breeds', breedService.getBreedsByType(PetType.DOG))
    modelAndView.addObject('breedsByTypeUrl', breedsByTypeUrl)
    modelAndView
  }

  @RequestMapping(method = GET, value = "/list")
  ModelAndView list() {
    log.info 'Listing pets'
    ModelAndView modelAndView = new ModelAndView()
    fillPetAndImageUrl(modelAndView)
  }

  @RequestMapping(method = GET, value = "/giveForAdoption")
  ModelAndView giveForAdoption() {
    log.info 'Select a pet for adoption'
    ModelAndView modelAndView = new ModelAndView('pet/giveForAdoption')
    fillPetAndImageUrl(modelAndView)
  }

  @RequestMapping(method = GET, value = "/listForAdoption")
  ModelAndView giveForAdoption() {
    log.info 'Listing pets for adoption'
    ModelAndView modelAndView = new ModelAndView('pet/listForAdoption')
    List<Pet> pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION)
    modelAndView.addObject('pets', pets)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

  private ModelAndView fillPetAndImageUrl(ModelAndView modelAndView){
    User user = userService.getCurrentUser()
    List<Pet> pets = petService.getPetsByUser(user)
    modelAndView.addObject('pets', pets)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

}
