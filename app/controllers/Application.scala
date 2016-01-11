package controllers

import dao.ContactDAO
import models.Contact
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import javax.inject.Inject

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = list()

  val contactForm = Form(
    tuple(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "emailAddress" -> nonEmptyText
    )
  )

  def list() = Action {
    Ok(views.html.index(ContactDAO.all(), contactForm))
  }

  def create() = Action { implicit request =>
    contactForm.bindFromRequest().fold(
      errors => BadRequest(views.html.index(ContactDAO.all(), errors)),
      {
        case (first_name, last_name, email_address) =>
          val contact = new Contact {
            firstName = first_name
            lastName = last_name
            emailAddress = email_address
          }
          ContactDAO.create(contact)
          Redirect(routes.Application.list())
      }
    )
  }

  def delete(id: Int) = Action {
    ContactDAO.delete(id)
    Redirect(routes.Application.list())
  }
}
