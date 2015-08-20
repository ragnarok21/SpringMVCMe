package org.ramon.controller;
import java.util.ArrayList;
import java.util.Arrays;
import org.ramon.clases.Persona;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.ModelMap;

@Controller
@Scope("session")
// MultiAtributtes to session
@SessionAttributes({ "Nombre", "Apellido","Username","Password" })
public class SuperController {
	private static Persona[] per = {
			new Persona("Jose", "P�rez", "joselote", "111444"),
			new Persona("Camilo", "Fernandez", "camilon123", "111233"),
			new Persona(" Ram�n", "Duran", "admin", "123455") };

	@RequestMapping("/inicio")
	public ModelAndView helloWorld() {
		return new ModelAndView("inicio", "message", "Inicio de Sesi�n");
	}

	@RequestMapping("/eliminar")
	public ModelAndView eliminar(@RequestParam(value="id")String getid,RedirectAttributes redirectAttributes) {
		ArrayList<Persona> personas = new ArrayList<Persona>(Arrays.asList(per));
		StringBuilder ids=new StringBuilder(getid);
		ids.deleteCharAt(0);
		ids.deleteCharAt(1);
	    String b=ids.toString();
	    int id=Integer.valueOf(b);
	    personas.remove(id);
	    ModelAndView m=new ModelAndView();
	    per=personas.toArray(new Persona[personas.size()]);
	    m.addObject("persons", personas);
	    m.addObject("username", "Username");
	    m.addObject("password", "Password");
	    m.setViewName("registrado");
		return m;
		
	}

	@RequestMapping("/editar")
	public ModelAndView alIndex() {
		return new ModelAndView("editar", "message", "Inicio de Sesi�n");
	}

	@RequestMapping("/registro")
	public ModelAndView registro() {
		return new ModelAndView("registro", "message", "Inicio de Sesi�n");
	}

	@RequestMapping(value = "/registrado", method = { RequestMethod.POST })
	public String inicioSesion(@RequestParam("username") String user,
			@RequestParam("password") String pass, Persona p, ModelMap model,
			RedirectAttributes redirectAttributes,SessionStatus sessionStatus) {
		Persona persona = buscarPersona(user, pass);
		/*
		if (user == "" && pass == "") {
			redirectAttributes.addFlashAttribute("userfail",
					"Campo necesario");
			redirectAttributes.addFlashAttribute("passfail",
					"Campo necesario");
			return "redirect:inicio";
		} else {

			if (user == "") {
				redirectAttributes.addFlashAttribute("userfail",
						"* Campo necesario");
				return "redirect:inicio";

			} else {
				if (pass == "") {
					redirectAttributes.addFlashAttribute("passfail",
							"* Campo necesario");
					return "redirect:inicio";
				} else {
*/
					if (persona != null) {

						if (persona.getUsername().equals("admin")) {
							
							ArrayList<Persona> personas = new ArrayList<Persona>(Arrays.asList(per));
							model.addAttribute("persons", personas);
							model.addAttribute("Nombre", " ");
							model.addAttribute("Apellido", " ");
							model.addAttribute("Username", p.getUsername());
							return "registrado";
						} else {
							model.addAttribute("bienvenido", "Bienvenido");
							model.addAttribute("Username", p.getUsername());
							model.addAttribute("Password", p.getPassword());
							model.addAttribute("Nombre", persona.getNombre());
							model.addAttribute("Apellido",persona.getApellido());
							return "registrado";
						}

					} else {
						redirectAttributes.addFlashAttribute("error","Usuario o password inv�lido");
						return "redirect:inicio";
					}

/*				}
			}
		}*/
	}

	// Comprobacion datos ingresados, solo se hara validacion en general si es
	// que todos los datos son vacios
	@RequestMapping(value = "/inicio", method = { RequestMethod.POST })
	public String compruebaRegistro(@RequestParam("name") String name,
			@RequestParam("lastn") String last,
			@RequestParam("username") String user,
			@RequestParam("password") String pass, ModelMap model,
			RedirectAttributes redirectAttributes) {
		/*
		if (name == "" && last == "" && user == "" && pass == "") {
			redirectAttributes.addFlashAttribute("namefail", "Campo inv�lido");
			redirectAttributes.addFlashAttribute("lastnfail", "Campo inv�lido");
			redirectAttributes.addFlashAttribute("userfail", "Campo inv�lido");
			redirectAttributes.addFlashAttribute("passfail", "Campo inv�lido");
			return "redirect:registro";
		} else {
		*/
			Persona p1 = new Persona(name, last, user, pass);
			Persona p2=buscarPersona1(user);
			if(p2!=null){
				redirectAttributes.addFlashAttribute("error", "* Su username esta en uso");
				return "redirect:registro";
			}else{
				Persona []aux=new Persona[per.length+1];
				System.arraycopy(per, 0, aux, 0, per.length);
				aux[aux.length-1]=p1;
				per=aux;
				model.addAttribute("exito", "Usuario creado exit�samente");
				model.addAttribute("message", "Inicio de Sesi�n");
				return "inicio";
			}
		}

	//}
	public static Persona buscarPersona(String username, String password) {
		Persona aux = null;
		int i = 0;
		while (i < per.length && aux == null) {
			if (per[i].getUsername().equals(username) && per[i].getPassword().equals(password)) {
				aux = per[i];
			} else {
				aux = null;
			}
			i++;
		}
		return aux;

	}
	public static Persona buscarPersona1(String username) {
		Persona aux = null;
		int i = 0;
		while (i < per.length && aux == null) {
			if (per[i].getUsername().equals(username)) {
				aux = per[i];
			} else {
				aux = null;
			}
			i++;
		}
		return aux;

	}
	
	

}