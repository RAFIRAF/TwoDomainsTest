package twodomainstest

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class Domain2Controller {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Domain2.list(params), model:[domain2Count: Domain2.count()]
    }

    def show(Domain2 domain2) {
        respond domain2
    }

    def create() {
        respond new Domain2(params)
    }

    @Transactional
    def save(Domain2 domain2) {
        if (domain2 == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (domain2.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond domain2.errors, view:'create'
            return
        }

        domain2.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'domain2.label', default: 'Domain2'), domain2.id])
                redirect domain2
            }
            '*' { respond domain2, [status: CREATED] }
        }
    }

    def edit(Domain2 domain2) {
        respond domain2
    }

    @Transactional
    def update(Domain2 domain2) {
        if (domain2 == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (domain2.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond domain2.errors, view:'edit'
            return
        }

        domain2.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'domain2.label', default: 'Domain2'), domain2.id])
                redirect domain2
            }
            '*'{ respond domain2, [status: OK] }
        }
    }

    @Transactional
    def delete(Domain2 domain2) {

        if (domain2 == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        domain2.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'domain2.label', default: 'Domain2'), domain2.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'domain2.label', default: 'Domain2'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
