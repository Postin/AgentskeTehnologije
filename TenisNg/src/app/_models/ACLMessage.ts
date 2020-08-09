import { AID } from "./AID";
import {PerformativeDTO} from './PerformativeDTO';

export class ACLMessage {
    performative: PerformativeDTO;
    sender: AID;
    receivers: AID[];
    replyTo: AID;
    content: string;
    contentObj: object;
    userArgs: Map<string, object>;
    language: string;
    encoding: string;
    ontology: string;
    protocol: string;
    conversationId: string;
    replyWith: string;
    inReplyTo: string;
    replyBy: number;

    constructor() {

    }

}
