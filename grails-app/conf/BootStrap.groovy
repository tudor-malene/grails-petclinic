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
            def ophthalmology = new Speciality(name: 'ophthalmology').save(failOnError: true)
            def pathology = new Speciality(name: 'pathology').save(failOnError: true)

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
            new Vet(firstName: 'Joe', lastName: 'Smith')
                    .addToSpecialities(radiology)
                    .save(failOnError: true)
            new Vet(firstName: 'Mary', lastName: 'Johnson')
                    .addToSpecialities(radiology)
                    .addToSpecialities(pathology)
                    .save(failOnError: true)
            new Vet(firstName: 'Patricia', lastName: 'Davis')
                    .addToSpecialities(pathology)
                    .save(failOnError: true)
            new Vet(firstName: 'Barbara', lastName: 'Jones')
                    .addToSpecialities(ophthalmology)
                    .save(failOnError: true)
            new Vet(firstName: 'Robert', lastName: 'Brown')
                    .addToSpecialities(radiology)
                    .save(failOnError: true)
            new Vet(firstName: 'Michael', lastName: 'Miller')
                    .addToSpecialities(ophthalmology)
                    .addToSpecialities(surgery)
                    .save(failOnError: true)
            new Vet(firstName: 'Thomas', lastName: 'Moore')
                    .addToSpecialities(radiology)
                    .save(failOnError: true)
            new Vet(firstName: 'Joseph', lastName: 'White')
                    .addToSpecialities(ophthalmology)
                    .addToSpecialities(surgery)
                    .save(failOnError: true)
            new Vet(firstName: 'Christopher', lastName: 'Jackson')
                    .addToSpecialities(radiology)
                    .save(failOnError: true)

            for (String type in ['dog', 'lizard', 'cat', 'snake', 'bird', 'hamster', 'fish']) {
                new PetType(name: type).save(failOnError: true)
            }

            def firstNames = []
            def lastNames = []
            def catNames = []
            def dogNames = []
            def cities = []

            def elemAt = { line, at ->
                line.split('\\t')[at].toLowerCase().capitalize()
            }

            def eachLineFromFile = { file, c ->
                this.class.getResourceAsStream(file).eachLine(c)
            }

            eachLineFromFile("first_names.txt"){
                firstNames << elemAt(it, 0)
            }
            eachLineFromFile("last_names.txt"){
                lastNames << elemAt(it, 0)
            }
            eachLineFromFile("cat_names.txt"){
                catNames << elemAt(it, 1)
                catNames << elemAt(it, 2)
            }
            eachLineFromFile("dog_names.txt"){
                dogNames << elemAt(it, 1)
                dogNames << elemAt(it, 2)
            }
            eachLineFromFile("cities.txt"){
                cities << elemAt(it, 0)
            }

            Random random = new Random()
            def rndElem = { list ->
                list[random.nextInt(list.size())]
            }

            def vets = Vet.list()

            //register 1000 random owners with pets & visits
            int N = 1000
            (1..N).each {
                Owner owner = new Owner(firstName: rndElem(firstNames), lastName: rndElem(firstNames), city: rndElem(cities), address: "some address ${random.nextInt(100)}", telephone: (1..10).collect{random.nextInt(9)}.join('')).save()
                if (owner) {
                    (0..(random.nextInt(3))).each {
                        def dog = new Pet(name: rndElem(dogNames), type: PetType.findByName('dog'), owner: owner, birthDate: new Date() - 365 * random.nextInt(20)).save()
                        if (!dog) {
                            return
                        }
                        owner.addToPets(dog).save(failOnError: true)
                        (0..(random.nextInt(4))).each {
                            dog.addToVisits(new Visit(date: new Date() - random.nextInt(500), description: 'routine', vet: rndElem(vets), pet: dog).save(failOnError: true)).save(failOnError: true)
                        }
                    }
                    (0..(random.nextInt(2))).each {
                        def cat = new Pet(name: rndElem(catNames), type: PetType.findByName('cat'), owner: owner, birthDate: new Date() - 365 * random.nextInt(15)).save()
                        if (!cat) {
                            return
                        }
                        owner.addToPets(cat).save(failOnError: true)
                        (0..(random.nextInt(3))).each {
                            cat.addToVisits(new Visit(date: new Date() - random.nextInt(500), description: 'routine', vet: rndElem(vets), pet: cat).save(failOnError: true)).save(failOnError: true)
                        }
                    }

                }
            }
        }

        NamedCriteriaProxy.metaClass.toDetachedCriteria = {
            def ncp = delegate
            new DetachedCriteria(ncp.domainClass.clazz).build {
                ncp.queryBuilder = delegate
                ncp.invokeCriteriaClosure()
            }
        }
        NamedCriteriaProxy.metaClass.toDetachedCriteria.delegate = Closure.OWNER_ONLY
    }
}
