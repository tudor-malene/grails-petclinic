import grails.gorm.DetachedCriteria
import org.codehaus.groovy.grails.orm.hibernate.cfg.NamedCriteriaProxy
import org.grails.samples.Owner
import org.grails.samples.Pet
import org.grails.samples.PetType
import org.grails.samples.Speciality
import org.grails.samples.Vet
import org.grails.samples.Visit

class BootStrap {

    def init = { servletContext ->
        if (!Speciality.count()) {
            def radiology = new Speciality(name: 'radiology').save(failOnError: true)
            def surgery = new Speciality(name: 'surgery').save(failOnError: true)
            def dentistry = new Speciality(name: 'dentistry').save(failOnError: true)

            new Vet(firstName: 'James', lastName: 'Carter').save(failOnError: true)
            new Vet(firstName: 'Helen', lastName: 'Leary')
                    .addToSpecialities(radiology)
                    .save(failOnError: true)
            new Vet(firstName: 'Linda', lastName: 'Douglas')
                    .addToSpecialities(surgery)
                    .addToSpecialities(dentistry)
                    .save(failOnError: true)
            new Vet(firstName: 'Rafael', lastName: 'Ortega')
                    .addToSpecialities(surgery)
                    .save(failOnError: true)
            new Vet(firstName: 'Henry', lastName: 'Stevens')
                    .addToSpecialities(radiology)
                    .save(failOnError: true)
            new Vet(firstName: 'Sharon', lastName: 'Jenkins').save(failOnError: true)

            for (String type in ['dog', 'lizard', 'cat', 'snake', 'bird', 'hamster']) {
                new PetType(name: type).save(failOnError: true)
            }

            def john = new Owner(firstName: 'John', lastName: 'Johnson', city: 'NY', address: '1st avenue', telephone: '111111').save(failOnError: true)
            def mary = new Owner(firstName: 'Mary', lastName: 'Doe', city: 'NJ', address: '2nd avenue', telephone: '222222').save(failOnError: true)
            def joe = new Owner(firstName: 'Joe', lastName: 'Doe', city: 'LA', address: '3rd avenue', telephone: '333333').save(failOnError: true)

            def bonkers = new Pet(name: 'Bonkers', type: PetType.findByName('dog'), owner: john, birthDate: new Date(), age:  1).save(failOnError: true)
            def tommy = new Pet(name: 'tommy', type: PetType.findByName('dog'), owner: john, birthDate: new Date(), age:  2).save(failOnError: true)
            def pandora = new Pet(name: 'pandora', type: PetType.findByName('cat'), owner: mary, birthDate: new Date(), age:  3).save(failOnError: true)
            def wanikiy = new Pet(name: 'wanikiy', type: PetType.findByName('bird'), owner: mary, birthDate: new Date(), age:  4).save(failOnError: true)
            def severin = new Pet(name: 'severin', type: PetType.findByName('hamster'), owner: joe, birthDate: new Date(), age:  5).save(failOnError: true)
            john.addToPets(bonkers).addToPets(tommy).save()
            mary.addToPets(pandora).addToPets(wanikiy).save()
            joe.addToPets(severin).save()

            bonkers.addToVisits(new Visit(description: 'routine', vet: Vet.findByLastName('Douglas'))).save()
            bonkers.addToVisits(new Visit(description: 'surgery', vet: Vet.findByLastName('Ortega'))).save()
            bonkers.addToVisits(new Visit(description: 'investigation', vet: Vet.findByLastName('Stevens'))).save()

        }

        NamedCriteriaProxy.metaClass.toDetachedCriteria = {
            def ncp =  delegate
            new DetachedCriteria(ncp.domainClass.clazz).build {
                ncp.queryBuilder = delegate
                ncp.invokeCriteriaClosure()
            }
        }
        NamedCriteriaProxy.metaClass.toDetachedCriteria.delegate = Closure.OWNER_ONLY
    }
}
